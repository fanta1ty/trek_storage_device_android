package sg.com.trekstorageauthentication.ble

import android.bluetooth.*
import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat
import no.nordicsemi.android.support.v18.scanner.ScanCallback
import no.nordicsemi.android.support.v18.scanner.ScanResult
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class BleServiceImpl(private val context: Context) : BleService {
    private val _response = MutableStateFlow(byteArrayOf())
    val response = _response.asStateFlow()

    private val scanner = BluetoothLeScannerCompat.getScanner()
    private var gatt: BluetoothGatt? = null
    private var scanCallback: ScanCallback? = null
    private var isConnected = false

    override fun isConnected() = isConnected

    override fun connect() {

    }

    override fun close() {
        scanCallback?.apply { scanner.stopScan(this) }
        scanCallback = null
        gatt?.close()
        gatt = null
        isConnected = false
    }

    override fun read(uuid: String) {
        val serviceUuid = UUID.fromString("SERVICE_UUID")
        val charUuid = UUID.fromString(uuid)
        val characteristic = gatt?.getService(serviceUuid)?.getCharacteristic(charUuid) ?: return
        gatt?.readCharacteristic(characteristic)
    }

    override fun write(uuid: String, bytes: ByteArray) {
        val serviceUuid = UUID.fromString("SERVICE_UUID")
        val charUuid = UUID.fromString(uuid)
        val characteristic = gatt?.getService(serviceUuid)?.getCharacteristic(charUuid) ?: return
        characteristic.writeType = BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT
        characteristic.value = bytes
        gatt?.writeCharacteristic(characteristic)
    }

    private suspend fun scan(): BluetoothDevice? {
        return suspendCoroutine { continuation ->
            scanCallback = object : ScanCallback() {
                override fun onScanResult(callbackType: Int, result: ScanResult) {
                    continuation.resume(result.device)
                }

                override fun onScanFailed(errorCode: Int) {
                    continuation.resume(null)
                }
            }.apply { scanner.startScan(this) }
        }
    }

    private fun connectGatt(device: BluetoothDevice) {
        gatt = device.connectGatt(context, false, object : BluetoothGattCallback() {
            override fun onConnectionStateChange(
                gatt: BluetoothGatt?,
                status: Int,
                newState: Int
            ) {
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    if (newState == BluetoothProfile.STATE_CONNECTED) {
                        isConnected = true
                        gatt?.apply { discoverServices() }
                    } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                        isConnected = false
                        close()
                    }
                } else {
                    isConnected = false
                    close()
                }
            }

            override fun onServicesDiscovered(
                gatt: BluetoothGatt?,
                status: Int
            ) = Unit

            override fun onCharacteristicRead(
                gatt: BluetoothGatt?,
                characteristic: BluetoothGattCharacteristic?,
                status: Int
            ) = Unit

            override fun onCharacteristicChanged(
                gatt: BluetoothGatt?,
                characteristic: BluetoothGattCharacteristic?
            ) = Unit
        })
    }

    private fun registerNotification() {
        val serviceUuid = UUID.fromString("SERVICE_UUID")
        val charUuid = UUID.fromString("ALERT_UUID")
        val descriptorUuid = UUID.fromString("ALERT_DESCRIPTOR_UUID")
        val characteristic = gatt?.getService(serviceUuid)?.getCharacteristic(charUuid) ?: return
        val descriptor = characteristic.getDescriptor(descriptorUuid) ?: return
        descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
        gatt?.setCharacteristicNotification(characteristic, true)
        gatt?.writeDescriptor(descriptor)
    }
}