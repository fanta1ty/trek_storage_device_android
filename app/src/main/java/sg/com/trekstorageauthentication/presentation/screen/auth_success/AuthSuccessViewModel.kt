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
) : ViewModel(), DataStoreService by DataStoreServiceImpl() {

    private val _unregisterDialogState = MutableStateFlow(AuthSuccessDialogState())
    val unregisterDialogState = _unregisterDialogState.asStateFlow()

    private val _resetDialogState = MutableStateFlow(AuthSuccessDialogState())
    val resetDialogState = _resetDialogState.asStateFlow()

    var thumbDriveFactoryResetting by mutableStateOf(false)
    var thumbDriveDisablingAuthentication by mutableStateOf(false)

    fun disableAuthentication() {
        dismissDisableAuthenticationDialog()
        viewModelScope.launch {
            if (bleService.isConnected()) {
                thumbDriveDisablingAuthentication = true
                bleService.write(
                    Constants.DISABLE_AUTHENTICATION_CHARACTERISTIC_UUID,
                    "TD_RESET".toByteArray()
                )
            } else {
                thumbDriveDisablingAuthentication = false
                val msg = context.getString(R.string.bluetooth_disconnected)
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun factoryResetThumbDrive() {
        dismissFactoryResetDialog()
        viewModelScope.launch {
            if (bleService.isConnected()) {
                thumbDriveFactoryResetting = true
                bleService.write(
                    Constants.FACTORY_RESET_CHARACTERISTIC_UUID,
                    "TD_RESET".toByteArray()
                )
            } else {
                thumbDriveFactoryResetting = false
                val msg = context.getString(R.string.bluetooth_disconnected)
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun showConfirmDisableAuthenticationDialog() {
        _unregisterDialogState.value = _unregisterDialogState.value.copy(isShowing = true)
    }

    fun showConfirmFactoryResetThumbDriveDialog() {
        _resetDialogState.value = _resetDialogState.value.copy(isShowing = true)
    }

    fun dismissDisableAuthenticationDialog() {
        _unregisterDialogState.value = AuthSuccessDialogState()
    }

    fun dismissFactoryResetDialog() {
        _resetDialogState.value = AuthSuccessDialogState()
    }

    fun getDataResponseEvent() = bleService.getDataResponseEvent()
}