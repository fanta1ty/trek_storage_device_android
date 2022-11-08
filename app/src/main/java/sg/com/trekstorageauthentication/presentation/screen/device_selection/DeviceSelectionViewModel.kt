package sg.com.trekstorageauthentication.presentation.screen.device_selection

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import sg.com.trekstorageauthentication.R
import sg.com.trekstorageauthentication.common.Constants
import sg.com.trekstorageauthentication.presentation.screen.device_selection.state.DeviceSelectionDialogState
import sg.com.trekstorageauthentication.service.ble.BleConnectionState
import sg.com.trekstorageauthentication.service.ble.BleService
import sg.com.trekstorageauthentication.service.datastore.DataStoreService
import sg.com.trekstorageauthentication.service.datastore.DataStoreServiceImpl
import sg.com.trekstorageauthentication.service.permission.PermissionService
import sg.com.trekstorageauthentication.service.permission.StoragePermissionServiceImpl
import java.util.*
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class DeviceSelectionViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val bleService: BleService
) : ViewModel(),
    DataStoreService by DataStoreServiceImpl(),
    PermissionService by StoragePermissionServiceImpl() {

    private val _dialogState = MutableStateFlow(DeviceSelectionDialogState())
    val dialogState = _dialogState.asStateFlow()

    private val _trekDevices = mutableStateListOf<BluetoothDevice>()
    val trekDevice: List<BluetoothDevice> = _trekDevices

    init {
        registerTrekDeviceEmittedEvent()
        registerBleConnectionEvent()
    }

    fun startScan(permissionResult: Boolean) {
        if (!permissionResult) {
            _dialogState.apply { value = value.copy(isShowPermissionsRequestDialog = true) }
            return
        } else {
            if (_dialogState.value.isShowPermissionsRequestDialog) dismissDialog()
        }

        if (!bleService.isLocationServiceEnabled()) {
            _dialogState.apply { value = value.copy(isShowLocationServiceDisabledDialog = true) }
            return
        } else {
            if (_dialogState.value.isShowLocationServiceDisabledDialog) dismissDialog()
        }

        if (!bleService.isBluetoothEnabled()) {
            _dialogState.apply { value = value.copy(isShowBluetoothDisabledDialog = true) }
            return
        } else {
            if (_dialogState.value.isShowBluetoothDisabledDialog) dismissDialog()
        }

        _trekDevices.clear()
        bleService.startScan()
    }

    fun stopScan() {
        bleService.stopScan()
    }

    fun connect(index: Int) {
        bleService.connect(_trekDevices[index])
    }

    fun dismissDialog() {
        _dialogState.value = DeviceSelectionDialogState()
    }

    fun getIsScanningState() = bleService.getIsScanningState()

    fun isLocationServiceEnabled() = bleService.isLocationServiceEnabled()

    fun getDataResponseEvent() = bleService.getDataResponseEvent()

    fun login() {
        viewModelScope.launch {
            val data = getStoredPin(context).toByteArray()
            bleService.write(Constants.LOG_IN_CHARACTERISTIC_UUID, data)
        }
    }

    fun readPinStatus() {
        bleService.read(Constants.READ_PIN_STATUS_CHARACTERISTIC_UUID)
    }

    private fun sendPhoneName() {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        val phoneName = if (model.lowercase().startsWith(manufacturer.lowercase())) {
            model.uppercase()
        } else {
            "${manufacturer.uppercase()} ${model.uppercase()}"
        }

        bleService.write(Constants.SEND_PHONE_NAME_UUID, phoneName.toByteArray())
    }

    private fun registerTrekDeviceEmittedEvent() {
        viewModelScope.launch {
            bleService.getTrekDeviceEmitEvent().collect { _trekDevices.add(it) }
        }
    }

    private fun registerBleConnectionEvent() {
        viewModelScope.launch {
            bleService.getBleConnectionEvent().collect { connectionState ->
                when (connectionState) {
                    BleConnectionState.CONNECTING -> {
                        _dialogState.apply { value = value.copy(isShowLoadingDialog = true) }
                    }

                    BleConnectionState.CONNECTED -> {
                        dismissDialog()
                        sendPhoneName()
                    }

                    BleConnectionState.ERROR -> {
                        dismissDialog()
                        val msg = context.getString(R.string.bluetooth_disconnected)
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}