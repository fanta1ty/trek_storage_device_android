package sg.com.trekstorageauthentication.service.datastore

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey

class DataStoreServiceImpl(private val context: Context) : DataStoreService {
    override suspend fun <T> dataStoreRead(key: String): T? {
        TODO("Not yet implemented")
    }

    override suspend fun <V> dataStoreWrite(key: String, value: V) {
        val dataStoreKey = stringPreferencesKey(key)

    }
}