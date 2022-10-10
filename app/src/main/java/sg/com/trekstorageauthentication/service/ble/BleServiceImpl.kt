package sg.com.trekstorageauthentication.service.ble

import android.annotation.SuppressLint
import android.bluetooth.*
import android.content.Context
import android.location.LocationManager
import android.os.Build
import android.os.ParcelUuid
import android.util.Log
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat
import no.nordicsemi.android.support.v18.scanner.ScanCallback
import no.nordicsemi.android.support.v18.scanner.ScanResult
import sg.com.trekstorageauthentication.common.Constants
import java.util.*

@SuppressLint("MissingPermission")
@Suppress("DEPRECATION")
class BleServiceImpl(private val context: Context) : BleService {

    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private val alreadyEmittedDevices = mutableListOf<Int>()
    private val trekDeviceEmitEvent = Channel<BluetoothDevice>()
    private val isScanning = MutableStateFlow(false)

    private val scanner = BluetoothLeScannerCompat.getScanner()
    private var gatt: BluetoothGatt? = null
    private var isConnected = false
    private var scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            val isTrekBleDevice = result.scanRecord?.serviceUuids
                ?.contains(ParcelUuid.fromString(Constants.SERVICE_UUID)) ?: false
            val addressCodes = result.device.address.hashCode()

            if (isTrekBleDevice && !alreadyEmittedDevices.contains(addressCodes)) {
                alreadyEmittedDevices.add(addressCodes)
                coroutineScope.launch { trekDeviceEmitEvent.send(result.device) }
            }
        }

        override fun onScanFailed(errorCode: Int) {
            close()
        }
    }

    override fun startScan() {
        if (isScanning.value) return
        isScanning.value = true
        scanner.startScan(scanCallback)

        coroutineScope.launch {
            delay(15000)
            stopScan()
        }
    }

    override fun stopScan() {
        if (isScanning.value) {
            isScanning.value = false
            scanner.stopScan(scanCallback)
        }
    }

    override fun connect() {

    }

    override fun close() {
        stopScan()
        gatt?.close()
        gatt = null
        isConnected = false
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

    override fun isConnected() = isConnected

    override fun getIsScanningState() = isScanning.asStateFlow()

    override fun getTrekDeviceEmitEvent() = trekDeviceEmitEvent.receiveAsFlow()

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
                        //bleConnectionListener?.invoke(BleConnectionState.CONNECTED)
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
//                    bleDataResponseListener?.invoke(
//                        onCharacteristicChangedResponse(String(value).toInt())
//                    )
                }
            }

            //Android 13 API
            override fun onCharacteristicChanged(
                gatt: BluetoothGatt,
                characteristic: BluetoothGattCharacteristic,
                value: ByteArray
            ) {
//                bleDataResponseListener?.invoke(
//                    onCharacteristicChangedResponse(String(value).toInt())
//                )
            }

            @Deprecated("Deprecated in Android 13")
            override fun onCharacteristicRead(
                gatt: BluetoothGatt?,
                characteristic: BluetoothGattCharacteristic?,
                status: Int
            ) {
                characteristic?.apply { onCharacteristicReadResponse(uuid.toString(), value) }
            }

            //Android 13 API
            override fun onCharacteristicRead(
                gatt: BluetoothGatt,
                characteristic: BluetoothGattCharacteristic,
                value: ByteArray,
                status: Int
            ) {
                onCharacteristicReadResponse(characteristic.uuid.toString(), value)
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
            4 -> Pair(BleResponseType.LOG_IN_SUCCESS, byteArrayOf())
            5 -> Pair(BleResponseType.LOG_IN_FAIL, byteArrayOf())
            6 -> Pair(BleResponseType.RESET_SETTINGS_SUCCESS, byteArrayOf())
            else -> Pair(BleResponseType.RESET_SETTINGS_FAIL, byteArrayOf())
        }
    }

    private fun onCharacteristicReadResponse(uuid: String, value: ByteArray) {
        when (uuid) {
            Constants.NOTIFICATION_CHARACTERISTIC_UUID -> {
                val responseType = if (String(value).toInt() == 4)
                    BleResponseType.ALREADY_LOG_IN
                else
                    BleResponseType.NOT_ALREADY_LOG_IN

                //bleDataResponseListener?.invoke(Pair(responseType, byteArrayOf()))
            }

            Constants.READ_PASSWORD_STATUS_CHARACTERISTIC_UUID -> {
                //bleDataResponseListener?.invoke(Pair(BleResponseType.PASSWORD_STATUS, value))
            }

            else -> Unit
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