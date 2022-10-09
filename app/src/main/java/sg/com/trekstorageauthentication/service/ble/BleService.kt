package sg.com.trekstorageauthentication.service.ble

interface BleService {
    fun connect()

    fun close()

    fun read(uuid: String)

    fun write(uuid: String, bytes: ByteArray)

    fun isConnected(): Boolean

    fun isBluetoothEnabled(): Boolean

    fun isLocationServiceEnabled(): Boolean
}