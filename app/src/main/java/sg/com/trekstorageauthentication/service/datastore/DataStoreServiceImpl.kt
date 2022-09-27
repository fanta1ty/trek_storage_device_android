package sg.com.trekstorageauthentication.service.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import sg.com.trekstorageauthentication.common.Constants
import java.util.*

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(Constants.DATA_STORE_NAME)

class DataStoreServiceImpl : DataStoreService {
    override suspend fun getStoredPassword(context: Context): String {
        val key = stringPreferencesKey(Constants.DATA_STORE_PASSWORD_KEY)
        val preferences = context.dataStore.data.first()
        return preferences[key] ?: ""
    }

    override suspend fun saveStoredPassword(context: Context, password: String) {
        context.dataStore.edit { preferences ->
            val key = stringPreferencesKey(Constants.DATA_STORE_PASSWORD_KEY)
            preferences[key] = password
        }
    }

    override fun generateUniqueIdentifier(): String {
        val timestamp = Date().hashCode().toString()
        val uuid = UUID.randomUUID().toString()
        return uuid + timestamp
    }
}