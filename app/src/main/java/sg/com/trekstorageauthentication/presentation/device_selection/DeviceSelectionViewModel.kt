package sg.com.trekstorageauthentication.presentation.device_selection

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
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

    private val bleDevices = mutableStateListOf<BluetoothDevice>()

    fun connectBle(permissionResult: Boolean) {
        if (permissionResult) {
            bleService.connect()
        }
    }

    fun isLocationServiceEnabled() = bleService.isLocationServiceEnabled()
}