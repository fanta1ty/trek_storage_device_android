package sg.com.trekstorageauthentication.service.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import sg.com.trekstorageauthentication.common.Constants

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(Constants.DATA_STORE_NAME)

class DataStoreServiceImpl : DataStoreService {
    override suspend fun getStoredPin(context: Context): String {
        val key = stringPreferencesKey(Constants.DATA_STORE_PIN_KEY)
        val preferences = context.dataStore.data.first()
        return preferences[key] ?: ""
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