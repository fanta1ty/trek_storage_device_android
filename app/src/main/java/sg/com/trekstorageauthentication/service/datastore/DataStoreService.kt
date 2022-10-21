package sg.com.trekstorageauthentication.service.datastore

import android.content.Context

interface DataStoreService {
    suspend fun getStoredPin(context: Context): String

    suspend fun saveStoredPin(context: Context, pin: String)

    fun generateUniqueIdentifier(): String
}