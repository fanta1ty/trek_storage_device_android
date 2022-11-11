package sg.com.trekstorageauthentication.service.ble

import android.annotation.SuppressLint
import android.bluetooth.*
import android.content.Context
import android.location.LocationManager
import android.os.Build
import android.os.ParcelUuid
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat
import no.nordicsemi.android.support.v18.scanner.ScanCallback
import no.nordicsemi.android.support.v18.scanner.ScanResult
import sg.com.trekstorageauthentication.R
import sg.com.trekstorageauthentication.common.Constants
import java.util.*

@SuppressLint("MissingPermission")
@Suppress("DEPRECATION")
class BleServiceImpl(private val context: Context) : BleService {

    companion object {
        private const val TAG = "BleService"
    }

    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private val alreadyEmittedDevices = mutableListOf<Int>()
    private val trekDeviceEmitEvent = Channel<BluetoothDevice>()
    private val bleConnectionEvent = Channel<BleConnectionState>()
    private val dataResponseEvent = Channel<Pair<BleResponseType, ByteArray>>()
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
            coroutineScope.launch { bleConnectionEvent.send(BleConnectionState.ERROR) }
        }
    }

    override fun startScan() {
        if (isScanning.value) return
        isScanning.value = true
        alreadyEmittedDevices.clear()
        scanner.startScan(scanCallback)

        coroutineScope.launch {
            delay(30000)
            stopScan()
        }
    }

    override fun stopScan() {
        if (isScanning.value) {
            isScanning.value = false
            scanner.stopScan(scanCallback)
        }
    }

    override fun connect(device: BluetoothDevice) {
        coroutineScope.launch {
            close()
            bleConnectionEvent.send(BleConnectionState.CONNECTING)
            gatt = device.connectGatt(context, false, getGattCallback())
        }
    }

    override fun close() {
        stopScan()
        gatt?.close()
        gatt = null
        isConnected = false
    }

    override fun disconnect() {
        gatt?.disconnect()
    }

    override fun read(uuid: String) {
        val serviceUuid = UUID.fromString(Constants.SERVICE_UUID)
        val charUuid = UUID.fromString(uuid)
        val characteristic = gatt?.getService(serviceUuid)?.getCharacteristic(charUuid) ?: return
        gatt?.readCharacteristic(characteristic)
    }

    override fun write(uuid: String, bytes: ByteArray) {
        Log.d(TAG, "Ble write: ${bytesToHex(bytes)}")

        val serviceUuid = UUID.fromString(Constants.SERVICE_UUID)
        val charUuid = UUID.fromString(uuid)
        val characteristic = gatt?.getService(serviceUuid)?.getCharacteristic(charUuid) ?: return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            gatt?.writeCharacteristic(
                characteristic, bytes, BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT
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

    override fun getBleConnectionEvent() = bleConnectionEvent.receiveAsFlow()

    override fun getDataResponseEvent() = dataResponseEvent.receiveAsFlow()

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
                        Toast.makeText(context, context.getString(R.string.bluetooth_disconnected), Toast.LENGTH_SHORT).show()
                        close()
                        coroutineScope.launch { bleConnectionEvent.send(BleConnectionState.ERROR) }
                    }
                } else {
                    close()
                    coroutineScope.launch { bleConnectionEvent.send(BleConnectionState.ERROR) }
                }
            }

            override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
                coroutineScope.launch {
                    registerNotification()
                    delay(1000)
                    bleConnectionEvent.send(BleConnectionState.CONNECTED)
                }
            }

            @Deprecated("Android 12 and lower")
            override fun onCharacteristicChanged(
                gatt: BluetoothGatt?,
                characteristic: BluetoothGattCharacteristic?
            ) {
                characteristic?.apply {
                    coroutineScope.launch {
                        val responseType = String(value).toInt()
                        dataResponseEvent.send(onCharacteristicChangedResponse(responseType))
                    }
                }
            }

            //Android 13 API
            override fun onCharacteristicChanged(
                gatt: BluetoothGatt,
                characteristic: BluetoothGattCharacteristic,
                value: ByteArray
            ) {
                coroutineScope.launch {
                    dataResponseEvent.send(onCharacteristicChangedResponse(String(value).toInt()))
                }
            }

            @Deprecated("Android 12 and lower")
            override fun onCharacteristicRead(
                gatt: BluetoothGatt?,
                characteristic: BluetoothGattCharacteristic?,
                status: Int
            ) {
                characteristic?.apply {
                    onCharacteristicReadResponse(uuid.toString(), value)
                    println("$uuid ${bytesToHex(value)}")
                }
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
        Log.d(TAG, "Response Type: $responseType")

        return when (responseType) {
            2 -> Pair(BleResponseType.REGISTER_PIN_SUCCESS, byteArrayOf())
            3 -> Pair(BleResponseType.REGISTER_PIN_FAIL, byteArrayOf())
            4 -> Pair(BleResponseType.LOG_IN_SUCCESS, byteArrayOf())
            5 -> Pair(BleResponseType.LOG_IN_FAIL, byteArrayOf())
            6 -> Pair(BleResponseType.DISABLE_AUTHENTICATION_SUCCESS, byteArrayOf())
            7 -> Pair(BleResponseType.DISABLE_AUTHENTICATION_FAILED, byteArrayOf())
            8 -> Pair(BleResponseType.FACTORY_RESET_SETTINGS_SUCCESS, byteArrayOf())
            9 -> Pair(BleResponseType.FACTORY_RESET_SETTINGS_FAIL, byteArrayOf())
            else -> Pair(BleResponseType.PHONE_NAME_SENT, byteArrayOf())
        }
    }

    private fun onCharacteristicReadResponse(uuid: String, value: ByteArray) {
        when (uuid) {
            Constants.READ_PIN_STATUS_CHARACTERISTIC_UUID -> {
                coroutineScope.launch {
                    val data = Pair(BleResponseType.PIN_STATUS, value)
                    dataResponseEvent.send(data)
                }
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