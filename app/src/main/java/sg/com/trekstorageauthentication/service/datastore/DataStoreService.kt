package sg.com.trekstorageauthentication.service.datastore

import android.content.Context

interface DataStoreService {
    suspend fun getStoredPin(context: Context): String

    suspend fun saveStoredPin(context: Context, pin: String)

    suspend fun getLastConnectedDeviceName(context: Context): String

    suspend fun saveLastConnectedDeviceName(context: Context, deviceName: String)
}