package sg.com.trekstorageauthentication.service.datastore

import android.content.Context

interface DataStoreService {
    suspend fun getStoredPassword(context: Context): String

    suspend fun saveStoredPassword(context: Context, password: String)

    fun generateUniqueIdentifier(): String
}