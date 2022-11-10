package sg.com.trekstorageauthentication.service.datastore

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import sg.com.trekstorageauthentication.common.Constants

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(Constants.DATA_STORE_NAME)

class DataStoreServiceImpl : DataStoreService {

    /**
     * Read stored PIN from shared preference.
     * If no PIN is stored, take the ANDROID_ID, save it and use it as PIN.
     * This will make sure the app on the same device will return the same ANDROID_ID
     * unless user factory reset the device.
     */
    @SuppressLint("HardwareIds")
    override suspend fun getStoredPin(context: Context): String {
        val key = stringPreferencesKey(Constants.DATA_STORE_PIN_KEY)
        val preferences = context.dataStore.data.first()
        return preferences[key] ?: Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        ).apply { saveStoredPin(context, this) }
    }

    override suspend fun saveStoredPin(context: Context, pin: String) {
        context.dataStore.edit { preferences ->
            val key = stringPreferencesKey(Constants.DATA_STORE_PIN_KEY)
            preferences[key] = pin
        }
    }

    override suspend fun getLastConnectedDeviceName(context: Context): String {
        val key = stringPreferencesKey(Constants.DATA_STORE_LAST_CONNECTED_DEVICE_NAME_KEY)
        val preferences = context.dataStore.data.first()
        return preferences[key] ?: ""
    }

    override suspend fun saveLastConnectedDeviceName(context: Context, deviceName: String) {
        context.dataStore.edit { preferences ->
            val key = stringPreferencesKey(Constants.DATA_STORE_LAST_CONNECTED_DEVICE_NAME_KEY)
            preferences[key] = deviceName
        }
    }
}