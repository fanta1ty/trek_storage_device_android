package sg.com.trekstorageauthentication.presentation

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.material.SnackbarDuration
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import sg.com.trekstorageauthentication.R
import sg.com.trekstorageauthentication.presentation.main.state.MainState
import sg.com.trekstorageauthentication.presentation.main.state.SnackbarEvent
import sg.com.trekstorageauthentication.service.ble.BleConnectionState
import sg.com.trekstorageauthentication.service.ble.BleService
import sg.com.trekstorageauthentication.service.permission.PermissionService
import sg.com.trekstorageauthentication.service.permission.StoragePermissionServiceImpl
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val bleService: BleService
) : ViewModel(), PermissionService by StoragePermissionServiceImpl() {

    private val _mainState = mutableStateOf(MainState())
    val mainState: State<MainState>
        get() = _mainState

    private val _snackbarEvent = Channel<SnackbarEvent>()
    val snackbarEvent = _snackbarEvent.receiveAsFlow()

    init {
        bleService.setBleConnectionListener(this::onBleConnectionEvent)
    }

    fun isLocationServiceEnabled() = bleService.isLocationServiceEnabled()

    fun connectBle(permissionResult: Boolean) {
        if (!permissionResult) {
            _mainState.value = _mainState.value.copy(isPermissionsGranted = false)
            return
        } else {
            //Permission denied dialog is still showing
            //after user has granted permissions in the Settings
            if (!_mainState.value.isPermissionsGranted) resetMainState()
        }

        if (!bleService.isLocationServiceEnabled()) {
            _mainState.value = _mainState.value.copy(isLocationServiceEnabled = false)
            return
        } else {
            if (!_mainState.value.isLocationServiceEnabled) resetMainState()
        }

        if (!bleService.isBluetoothEnabled()) {
            _mainState.value = _mainState.value.copy(isBluetoothEnabled = false)
            onBleConnectionEvent(BleConnectionState.DISCONNECTED)
            return
        } else {
            if (!_mainState.value.isBluetoothEnabled) resetMainState()
        }

        bleService.connect()
    }

    fun resetMainState() {
        _mainState.value = MainState()
    }

    private fun onBleConnectionEvent(connectionState: BleConnectionState) {
        viewModelScope.launch {
            when (connectionState) {
                BleConnectionState.CONNECTING -> {
                    val msg = context.getString(R.string.connecting)
                    _snackbarEvent.send(SnackbarEvent(msg, SnackbarDuration.Indefinite))
                }

                BleConnectionState.DISCONNECTED -> {
                    val msg = context.getString(R.string.disconnected)
                    _snackbarEvent.send(SnackbarEvent(msg, SnackbarDuration.Indefinite))
                }

                //BleConnectionState.CONNECTED
                else -> _snackbarEvent.send(SnackbarEvent(""))
            }
        }
    }
}