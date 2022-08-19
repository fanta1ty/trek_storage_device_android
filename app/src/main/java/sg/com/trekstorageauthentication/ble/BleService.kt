package sg.com.trekstorageauthentication.ble

interface BleService {
    fun connect()

    fun close()

    fun read(uuid: String)

    fun write(uuid: String, bytes: ByteArray)

    fun isConnected(): Boolean
}