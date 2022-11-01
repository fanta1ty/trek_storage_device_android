package sg.com.trekstorageauthentication.presentation.screen.auth_success

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import sg.com.trekstorageauthentication.R
import sg.com.trekstorageauthentication.common.Constants
import sg.com.trekstorageauthentication.presentation.screen.auth_success.state.AuthSuccessDialogState
import sg.com.trekstorageauthentication.service.ble.BleService
import javax.inject.Inject

@HiltViewModel
@SuppressLint("StaticFieldLeak")
class AuthSuccessViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val bleService: BleService
) : ViewModel() {

    private val _dialogState = MutableStateFlow(AuthSuccessDialogState())
    val dialogState = _dialogState.asStateFlow()

    fun resetThumbDrive() {
        dismissDialog()

        if (bleService.isConnected()) {
            bleService.write(Constants.RESET_THUMB_DRIVE_CHARACTERISTIC_UUID, "1".toByteArray())
        } else {
            val msg = context.getString(R.string.bluetooth_disconnected)
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
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