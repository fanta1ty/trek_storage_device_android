package sg.com.trekstorageauthentication.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
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
import sg.com.trekstorageauthentication.common.Constants
import sg.com.trekstorageauthentication.presentation.main.state.MainState
import sg.com.trekstorageauthentication.presentation.main.state.NavigationEvent
import sg.com.trekstorageauthentication.presentation.main.state.SnackbarEvent
import sg.com.trekstorageauthentication.presentation.ui.navigation.Screen
import sg.com.trekstorageauthentication.service.ble.BleConnectionState
import sg.com.trekstorageauthentication.service.ble.BleResponseType
import sg.com.trekstorageauthentication.service.ble.BleService
import sg.com.trekstorageauthentication.service.datastore.DataStoreService
import sg.com.trekstorageauthentication.service.datastore.DataStoreServiceImpl
import sg.com.trekstorageauthentication.service.permission.PermissionService
import sg.com.trekstorageauthentication.service.permission.StoragePermissionServiceImpl
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val bleService: BleService
) : ViewModel(),
    PermissionService by StoragePermissionServiceImpl(),
    DataStoreService by DataStoreServiceImpl() {

    private val _mainState = mutableStateOf(MainState())
    val mainState: State<MainState>
        get() = _mainState

    private val _snackbarEvent = Channel<SnackbarEvent>()
    val snackbarEvent = _snackbarEvent.receiveAsFlow()

    private val _biometricAuthEvent = Channel<Unit>()
    val biometricAuthEvent = _biometricAuthEvent.receiveAsFlow()

    private val _navigationEvent = Channel<NavigationEvent>()
    val navigationEvent = _navigationEvent.receiveAsFlow()

    private var authPassword = ""

    init {
        Log.d("Debug", "init mainviewmodel")
        bleService.apply {
            setBleConnectionListener(this@MainViewModel::onBleConnectionListener)
            setBleDataResponseListener(this@MainViewModel::onBleDataResponseListener)
        }
    }

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
            onBleConnectionListener(BleConnectionState.DISCONNECTED)
            return
        } else {
            if (!_mainState.value.isBluetoothEnabled) resetMainState()
        }

        if (!bleService.isConnected()) {
            bleService.connect()
        } else {
            performBiometricAuth()
        }
    }

    fun readBleData(uuid: String) {
        bleService.read(uuid)
    }

    fun sendBleData(uuid: String, byteArray: ByteArray) {
        bleService.write(uuid, byteArray)
    }

    fun isLocationServiceEnabled() = bleService.isLocationServiceEnabled()

    fun resetMainState() {
        _mainState.value = MainState()
    }

    fun unlockTrekStorage(password: String) {
        this.authPassword = password
        sendBleData(Constants.UNLOCK_PASSWORD_CHARACTERISTIC_UUID, password.toByteArray())
    }

    fun logOut() {
        sendBleData(Constants.LOG_OUT_CHARACTERISTIC_UUID, "01".toByteArray())
    }

    fun navigate(route: String = "", popUpToRoute: String = "", isInclusive: Boolean = true) {
        viewModelScope.launch {
            _navigationEvent.send(NavigationEvent(route, popUpToRoute, isInclusive))
        }
    }

    private fun onBleDataResponseListener(response: Pair<BleResponseType, ByteArray>) {
        val (type, data) = response
        Log.d("Debug", "onBleDataResponseListener: ${type.name}")
        when (type) {
            BleResponseType.REGISTER_PASSWORD_SUCCESS -> {
                showSnackbar(SnackbarEvent(context.getString(R.string.register_password_success)))
                navigate(Screen.UnlockScreen.route, Screen.RegisterPasswordScreen.route)
            }

            BleResponseType.REGISTER_PASSWORD_FAIL -> {
                showSnackbar(SnackbarEvent(context.getString(R.string.register_password_fail)))
            }

            BleResponseType.RESET_PASSWORD_SUCCESS -> {
                showSnackbar(SnackbarEvent(context.getString(R.string.reset_password_success)))
                viewModelScope.launch {
                    saveStoredPassword(context, "")
                }
                navigate(Screen.RegisterPasswordScreen.route, Screen.UnlockScreen.route, true)
            }

            BleResponseType.RESET_PASSWORD_FAIL -> {
                showSnackbar(SnackbarEvent(context.getString(R.string.reset_password_fail)))
            }

            BleResponseType.UNLOCK_PASSWORD_SUCCESS -> {
                viewModelScope.launch { saveStoredPassword(context, authPassword) }
                showSnackbar(SnackbarEvent(context.getString(R.string.unlock_storage_success)))
                navigate(Screen.HomeScreen.route, Screen.HomeScreen.route, true)
            }

            BleResponseType.UNLOCK_PASSWORD_FAIL -> {
                showSnackbar(SnackbarEvent(context.getString(R.string.unlock_storage_fail)))
            }

            BleResponseType.LOG_OUT_SUCCESS -> {
                showSnackbar(SnackbarEvent(context.getString(R.string.log_out_success)))
                navigate(Screen.UnlockScreen.route, Screen.UnlockScreen.route, true)
            }

            BleResponseType.LOG_OUT_FAIL -> {
                showSnackbar(SnackbarEvent(context.getString(R.string.log_out_fail)))
            }

            else -> { //BleResponseType.PASSWORD_STATUS
                when (String(data).toInt()) {
                    0 -> {
                        //User defined password
                        viewModelScope.launch {
                            authPassword = getStoredPassword(context)
                            authPassword.takeIf { it.isNotEmpty() }?.let { unlockTrekStorage(it) }
                        }
                    }

                    1 -> {
                        //Default password
                        navigate(Screen.RegisterPasswordScreen.route, Screen.UnlockScreen.route)
                    }

                    else -> {
                        val msg = context.getString(R.string.no_trek_devices_found)
                        showSnackbar(SnackbarEvent(msg))
                    }
                }
            }
        }
    }

    private fun onBleConnectionListener(connectionState: BleConnectionState) {
        when (connectionState) {
            BleConnectionState.CONNECTING -> {
                val msg = context.getString(R.string.connecting)
                showSnackbar(SnackbarEvent(msg, SnackbarDuration.Indefinite))
            }

            BleConnectionState.DISCONNECTED -> {
                val msg = context.getString(R.string.disconnected)
                showSnackbar(SnackbarEvent(msg, SnackbarDuration.Indefinite))
            }

            else -> { //BleConnectionState.CONNECTED
                showSnackbar(SnackbarEvent("")) //Send empty msg to dismiss snackbar
                performBiometricAuth()
            }
        }
    }

    private fun showSnackbar(event: SnackbarEvent) {
        viewModelScope.launch {
            val (msg, duration) = event
            _snackbarEvent.send(SnackbarEvent(msg, duration))
        }
    }

    private fun performBiometricAuth() {
        viewModelScope.launch { _biometricAuthEvent.send(Unit) }
    }
}