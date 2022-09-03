package sg.com.trekstorageauthentication.service.ble

import kotlinx.coroutines.flow.StateFlow

interface BleService {
    fun connect()

    fun close()

    fun read(uuid: String)

    fun write(uuid: String, bytes: ByteArray)

    fun isConnected(): Boolean

    fun getBleConnectionState(): StateFlow<BleConnectionState>

    fun isBluetoothEnabled(): Boolean

    fun isLocationServiceEnabled(): Boolean
}