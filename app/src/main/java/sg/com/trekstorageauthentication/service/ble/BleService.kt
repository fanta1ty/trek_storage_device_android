package sg.com.trekstorageauthentication.service.ble

import android.bluetooth.BluetoothDevice
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface BleService {
    fun startScan()

    fun stopScan()

    fun connect()

    fun close()

    fun read(uuid: String)

    fun write(uuid: String, bytes: ByteArray)

    fun isBluetoothEnabled(): Boolean

    fun isLocationServiceEnabled(): Boolean

    fun getIsScanningState(): StateFlow<Boolean>

    fun getTrekDeviceEmitEvent(): Flow<BluetoothDevice>

    fun isConnected(): Boolean
}