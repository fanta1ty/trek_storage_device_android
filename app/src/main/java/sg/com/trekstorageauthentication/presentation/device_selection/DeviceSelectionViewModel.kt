package sg.com.trekstorageauthentication.presentation.device_selection

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import sg.com.trekstorageauthentication.presentation.device_selection.state.DeviceSelectionDialogState
import sg.com.trekstorageauthentication.service.ble.BleService
import sg.com.trekstorageauthentication.service.permission.PermissionService
import sg.com.trekstorageauthentication.service.permission.StoragePermissionServiceImpl
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class DeviceSelectionViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val bleService: BleService
) : ViewModel(), PermissionService by StoragePermissionServiceImpl() {

    private val dialogHandler = MutableStateFlow(DeviceSelectionDialogState())
    private val isScanning = MutableStateFlow(false)
    private val _deviceList = MutableStateFlow(mutableListOf<BluetoothDevice>())
    val deviceList = _deviceList.asStateFlow()

    val deviceSelectionState =
        combine(dialogHandler, isScanning, _deviceList) { dialogState, scanningState, devices ->

        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    fun connectBle(permissionResult: Boolean) {

    }

    fun isLocationServiceEnabled() = bleService.isLocationServiceEnabled()
}