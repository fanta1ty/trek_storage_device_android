package sg.com.trekstorageauthentication.service.ble

import android.bluetooth.*
import android.content.Context
import android.location.LocationManager
import android.util.Log
import androidx.core.util.forEach
import no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat
import no.nordicsemi.android.support.v18.scanner.ScanCallback
import no.nordicsemi.android.support.v18.scanner.ScanResult
import java.util.*

class BleServiceImpl(private val context: Context) : BleService {
    private val scanner = BluetoothLeScannerCompat.getScanner()
    private var gatt: BluetoothGatt? = null
    private var isConnected = false
    private var isAlreadyScanning = false
    private var scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            val scanRecord = result.scanRecord ?: return


            if(scanRecord.deviceName == "Trek_ble"){
                scanner.stopScan(this)

                Log.e("HuyTest", "bytes: ${bytesToHex(scanRecord.bytes!!)}")
                Log.e("HuyTest", "manufacturerSpecificData: ${scanRecord.manufacturerSpecificData}")
                Log.e("HuyTest", "serviceData: ${scanRecord.serviceData}")
                Log.e("HuyTest", "serviceUuids: ${scanRecord.serviceUuids}")
            }

//            if (result.device.address == "58:11:D4:7B:34:B3") {
//                scanner.stopScan(this)
//                isAlreadyScanning = false
//                gatt = result.device.connectGatt(context, false, getGattCallback())
//            }
        }

        override fun onScanFailed(errorCode: Int) {
            close()
        }
    }

    override fun isConnected() = isConnected

    override suspend fun connect() {
        if (isAlreadyScanning) return

        isAlreadyScanning = true
        scanner.startScan(scanCallback)
    }

    override fun close() {
        scanner.stopScan(scanCallback)
        gatt?.close()
        gatt = null
        isConnected = false
        isAlreadyScanning = false
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