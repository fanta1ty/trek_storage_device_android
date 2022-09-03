package sg.com.trekstorageauthentication.service.ble

import android.bluetooth.*
import android.content.Context
import android.location.LocationManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat
import no.nordicsemi.android.support.v18.scanner.ScanCallback
import no.nordicsemi.android.support.v18.scanner.ScanResult
import java.util.*

class BleServiceImpl(private val context: Context) : BleService {

    private val scanner = BluetoothLeScannerCompat.getScanner()
    private var gatt: BluetoothGatt? = null
    private var isConnected = false
    private var isAlreadyScanning = false
    private val bleConnectionState = MutableStateFlow(BleConnectionState.CONNECTED)
    private var scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            if (result.device.name == "TREK_BLE") {
                scanner.stopScan(this)
                isAlreadyScanning = false
                gatt = result.device.connectGatt(context, false, getGattCallback())

//                Log.e("HuyTest", "bytes: ${bytesToHex(scanRecord.bytes!!)}")
//                Log.e("HuyTest", "manufacturerSpecificData: ${scanRecord.manufacturerSpecificData}")
//                Log.e("HuyTest", "serviceData: ${scanRecord.serviceData}")
//                Log.e("HuyTest", "serviceUuids: ${scanRecord.serviceUuids}")
            }
        }

        override fun onScanFailed(errorCode: Int) {
            close()
        }
    }

    override fun isConnected() = isConnected

    override fun getBleConnectionState() = bleConnectionState.asStateFlow()

    override fun connect() {
        if (isAlreadyScanning) return

        isAlreadyScanning = true
        bleConnectionState.value = BleConnectionState.CONNECTING
        scanner.startScan(scanCallback)
    }

    override fun close() {
        scanner.stopScan(scanCallback)
        gatt?.close()
        gatt = null
        isConnected = false
        isAlreadyScanning = false
        bleConnectionState.value = BleConnectionState.DISCONNECTED
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

    override fun isBluetoothEnabled(): Boolean {
        val bluetoothManager =
            context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        return bluetoothManager.adapter.isEnabled
    }

    override fun isLocationServiceEnabled(): Boolean {
        val locationManager = context.getSystemService(LocationManager::class.java)
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        return isGpsEnabled && isNetworkEnabled
    }

    //---------

    private fun getGattCallback(): BluetoothGattCallback {
        return object : BluetoothGattCallback() {

            override fun onConnectionStateChange(
                gatt: BluetoothGatt?,
                status: Int,
                newState: Int
            ) {
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    if (newState == BluetoothProfile.STATE_CONNECTED) {
                        isConnected = true
                        bleConnectionState.value = BleConnectionState.CONNECTED
                        gatt?.apply { discoverServices() }
                    } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                        close()
                    }
                } else {
                    close()
                }
            }

            override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
                registerNotification()
            }

            override fun onCharacteristicChanged(
                gatt: BluetoothGatt?,
                characteristic: BluetoothGattCharacteristic?
            ) = Unit
        }
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

    private fun bytesToHex(data: ByteArray, prefix: String = ""): String {
        val c = "0123456789ABCDEF".toCharArray()
        var result = ""
        data.forEach {
            result += c[it.toInt() and 255 shr 4]
            result += c[it.toInt() and 15]
            //result += ' '
        }
        return prefix + result
    }
}