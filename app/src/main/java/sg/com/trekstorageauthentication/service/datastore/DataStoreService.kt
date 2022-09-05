package sg.com.trekstorageauthentication.service.datastore

interface DataStoreService {
    suspend fun <T> dataStoreRead(key: String): T?

    suspend fun <V> dataStoreWrite(key: String, value: V)
}