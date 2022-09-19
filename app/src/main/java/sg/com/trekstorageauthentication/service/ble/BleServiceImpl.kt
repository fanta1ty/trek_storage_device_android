package sg.com.trekstorageauthentication.service.ble

import android.bluetooth.*
import android.content.Context
import android.location.LocationManager
import android.os.Build
import android.os.ParcelUuid
import android.util.Log
import no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat
import no.nordicsemi.android.support.v18.scanner.ScanCallback
import no.nordicsemi.android.support.v18.scanner.ScanResult
import sg.com.trekstorageauthentication.common.Constants
import java.util.*

@Suppress("DEPRECATION")
class BleServiceImpl(private val context: Context) : BleService {

    private val scanner = BluetoothLeScannerCompat.getScanner()
    private var gatt: BluetoothGatt? = null
    private var isConnected = false
    private var isAlreadyScanning = false
    private var bleConnectionListener: ((BleConnectionState) -> Unit)? = null
    private var bleDataResponseListener: ((Pair<BleResponseType, ByteArray>) -> Unit)? = null
    private var scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            val isTrekBleDevice = result.scanRecord?.serviceUuids
                ?.contains(ParcelUuid.fromString(Constants.SERVICE_UUID)) ?: false

            if (isTrekBleDevice) {
                scanner.stopScan(this)
                isAlreadyScanning = false
                gatt = result.device.connectGatt(
                    context, false, getGattCallback(), BluetoothDevice.TRANSPORT_LE
                )
            }
        }

        override fun onScanFailed(errorCode: Int) {
            close()
        }
    }

    override fun connect() {
        if (isAlreadyScanning) return

        isAlreadyScanning = true
        bleConnectionListener?.invoke(BleConnectionState.CONNECTING)
        scanner.startScan(scanCallback)
    }

    override fun close() {
        scanner.stopScan(scanCallback)
        gatt?.close()
        gatt = null
        isConnected = false
        isAlreadyScanning = false
        bleConnectionListener?.invoke(BleConnectionState.DISCONNECTED)
    }

    override fun read(uuid: String) {
        val serviceUuid = UUID.fromString(Constants.SERVICE_UUID)
        val charUuid = UUID.fromString(uuid)
        val characteristic = gatt?.getService(serviceUuid)?.getCharacteristic(charUuid) ?: return
        gatt?.readCharacteristic(characteristic)
    }

    override fun write(uuid: String, bytes: ByteArray) {
        Log.d("HuyTest", "Ble write: ${bytesToHex(bytes)}")

        val serviceUuid = UUID.fromString(Constants.SERVICE_UUID)
        val charUuid = UUID.fromString(uuid)
        val characteristic = gatt?.getService(serviceUuid)?.getCharacteristic(charUuid) ?: return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            gatt?.writeCharacteristic(
                characteristic,
                bytes,
                BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT
            )
        } else {
            characteristic.writeType = BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT
            characteristic.value = bytes
            gatt?.writeCharacteristic(characteristic)
        }
    }

    override fun isConnected() = isConnected

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

    override fun setBleConnectionListener(listener: (BleConnectionState) -> Unit) {
        bleConnectionListener = listener
    }

    override fun setBleDataResponseListener(listener: (Pair<BleResponseType, ByteArray>) -> Unit) {
        bleDataResponseListener = listener
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
                        bleConnectionListener?.invoke(BleConnectionState.CONNECTED)
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

            @Deprecated("Deprecated in Android 13")
            override fun onCharacteristicChanged(
                gatt: BluetoothGatt?,
                characteristic: BluetoothGattCharacteristic?
            ) {
                characteristic?.apply {
                    bleDataResponseListener?.invoke(
                        onCharacteristicChangedResponse(String(value).toInt())
                    )
                }
            }

            //Android 13 API
            override fun onCharacteristicChanged(
                gatt: BluetoothGatt,
                characteristic: BluetoothGattCharacteristic,
                value: ByteArray
            ) {
                bleDataResponseListener?.invoke(
                    onCharacteristicChangedResponse(String(value).toInt())
                )
            }

            @Deprecated("Deprecated in Android 13")
            override fun onCharacteristicRead(
                gatt: BluetoothGatt?,
                characteristic: BluetoothGattCharacteristic?,
                status: Int
            ) {
                characteristic?.apply {
                    bleDataResponseListener?.invoke(Pair(BleResponseType.PASSWORD_STATUS, value))
                }
            }

            //Android 13 API
            override fun onCharacteristicRead(
                gatt: BluetoothGatt,
                characteristic: BluetoothGattCharacteristic,
                value: ByteArray,
                status: Int
            ) {
                bleDataResponseListener?.invoke(Pair(BleResponseType.PASSWORD_STATUS, value))
            }

            override fun onCharacteristicWrite(
                gatt: BluetoothGatt?,
                characteristic: BluetoothGattCharacteristic?,
                status: Int
            ) {
                if (status != 0) close()
            }
        }
    }

    private fun registerNotification() {
        val serviceUuid = UUID.fromString(Constants.SERVICE_UUID)
        val charUuid = UUID.fromString(Constants.NOTIFICATION_CHARACTERISTIC_UUID)
        val descriptorUuid = UUID.fromString(Constants.NOTIFICATION_DESCRIPTOR_UUID)
        val characteristic = gatt?.getService(serviceUuid)?.getCharacteristic(charUuid) ?: return
        val descriptor = characteristic.getDescriptor(descriptorUuid) ?: return
        gatt?.setCharacteristicNotification(characteristic, true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            gatt?.writeDescriptor(descriptor, BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE)
        } else {
            descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
            gatt?.writeDescriptor(descriptor)
        }
    }

    private fun onCharacteristicChangedResponse(
        responseType: Int
    ): Pair<BleResponseType, ByteArray> {
        return when (responseType) {
            2 -> Pair(BleResponseType.REGISTER_PASSWORD_SUCCESS, byteArrayOf())
            3 -> Pair(BleResponseType.REGISTER_PASSWORD_FAIL, byteArrayOf())
            4 -> Pair(BleResponseType.UNLOCK_PASSWORD_SUCCESS, byteArrayOf())
            5 -> Pair(BleResponseType.UNLOCK_PASSWORD_FAIL, byteArrayOf())
            6 -> Pair(BleResponseType.RESET_PASSWORD_SUCCESS, byteArrayOf())
            7 -> Pair(BleResponseType.RESET_PASSWORD_FAIL, byteArrayOf())
            8 -> Pair(BleResponseType.LOG_OUT_SUCCESS, byteArrayOf())
            else -> Pair(BleResponseType.LOG_OUT_FAIL, byteArrayOf())
        }
    }

    private fun bytesToHex(data: ByteArray, prefix: String = ""): String {
        val c = "0123456789ABCDEF".toCharArray()
        var result = ""
        data.forEach {
            result += c[it.toInt() and 255 shr 4]
            result += c[it.toInt() and 15]
            result += ' '
        }
        return prefix + result
    }
}