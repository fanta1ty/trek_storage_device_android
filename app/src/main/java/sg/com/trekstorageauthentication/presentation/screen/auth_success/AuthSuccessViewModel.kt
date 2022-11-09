package sg.com.trekstorageauthentication.presentation.screen.auth_success

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import sg.com.trekstorageauthentication.R
import sg.com.trekstorageauthentication.common.Constants
import sg.com.trekstorageauthentication.presentation.screen.auth_success.state.AuthSuccessDialogState
import sg.com.trekstorageauthentication.service.ble.BleService
import sg.com.trekstorageauthentication.service.datastore.DataStoreService
import sg.com.trekstorageauthentication.service.datastore.DataStoreServiceImpl
import javax.inject.Inject

@HiltViewModel
@SuppressLint("StaticFieldLeak")
class AuthSuccessViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val bleService: BleService
) : ViewModel(),
    DataStoreService by DataStoreServiceImpl() {

    private val _dialogState = MutableStateFlow(AuthSuccessDialogState())
    val dialogState = _dialogState.asStateFlow()

    var thumbDriveResetting by mutableStateOf(false)

    fun resetThumbDrive() {
        dismissDialog()
        viewModelScope.launch {
            // Delete saved last connected device name
            saveLastConnectedDeviceName(context, "")

            if (bleService.isConnected()) {
                thumbDriveResetting = true
                bleService.write(Constants.RESET_THUMB_DRIVE_CHARACTERISTIC_UUID, "1".toByteArray())
            } else {
                thumbDriveResetting = false
                val msg = context.getString(R.string.bluetooth_disconnected)
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun showConfirmResetThumbDriveDialog() {
        _dialogState.value = _dialogState.value.copy(isShowConfirmResetThumbDriveDialog = true)
    }

    fun dismissDialog() {
        _dialogState.value = AuthSuccessDialogState()
    }

    fun getDataResponseEvent() = bleService.getDataResponseEvent()
}