package sg.com.trekstorageauthentication.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
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

    private val _biometricAuthEvent = Channel<String>()
    val biometricAuthEvent = _biometricAuthEvent.receiveAsFlow()

    private val _navigationEvent = Channel<NavigationEvent>()
    val navigationEvent = _navigationEvent.receiveAsFlow()

    private var authPassword = ""

    init {
        bleService.apply {
            setBleConnectionListener(this@MainViewModel::onBleConnectionListener)
            setBleDataResponseListener(this@MainViewModel::onBleDataResponseListener)
        }
    }

    fun isLocationServiceEnabled() = bleService.isLocationServiceEnabled()

    fun resetMainState() {
        _mainState.value = MainState()
    }

    fun unlockTrekStorage(password: String) {
        this.authPassword = password
        sendBleData(Constants.VERIFY_PASSWORD_CHARACTERISTIC_UUID, password.toByteArray())
    }

    fun readBleData(uuid: String) {
        bleService.read(uuid)
    }

    fun sendBleData(uuid: String, byteArray: ByteArray) {
        bleService.write(uuid, byteArray)
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

        bleService.connect()
    }

    private fun onBleDataResponseListener(response: Pair<BleResponseType, ByteArray>) {
        val (type, data) = response
        when (type) {
            BleResponseType.SET_PASSWORD_SUCCESS -> {
                showSnackbar(SnackbarEvent(context.getString(R.string.register_password_success)))
                viewModelScope.launch {
                    _navigationEvent.send(
                        NavigationEvent(
                            Screen.UnlockScreen.route,
                            Screen.RegisterPasswordScreen.route,
                            true
                        )
                    )
                }
            }

            BleResponseType.SET_PASSWORD_FAIL -> {
                showSnackbar(SnackbarEvent(context.getString(R.string.register_password_fail)))
            }

            BleResponseType.VERIFY_PASSWORD_SUCCESS -> {
                viewModelScope.launch { saveStoredPassword(context, authPassword) }
                showSnackbar(SnackbarEvent(context.getString(R.string.unlock_storage_success)))
            }

            BleResponseType.VERIFY_PASSWORD_FAIL -> {
                showSnackbar(SnackbarEvent(context.getString(R.string.unlock_storage_fail)))
            }

            else -> { //BleResponseType.PASSWORD
                val password = String(data)
                if (password.isEmpty()) {
                    //Newly bought storage
                    viewModelScope.launch {
                        _navigationEvent.send(
                            NavigationEvent(
                                Screen.RegisterPasswordScreen.route,
                                Screen.UnlockScreen.route,
                                true
                            )
                        )
                    }
                } else {
                    //Already set password
                    viewModelScope.launch {
                        authPassword = getStoredPassword(context)
                        authPassword.takeIf { it.isNotEmpty() }?.let { unlockTrekStorage(it) }
                    }
                }
            }
        }
    }

    private fun onBleConnectionListener(connectionState: BleConnectionState) {
        viewModelScope.launch {
            when (connectionState) {
                BleConnectionState.CONNECTING -> {
                    val msg = context.getString(R.string.connecting)
//                    _snackbarEvent.send(SnackbarEvent(msg))
//                    showSnackbar(SnackbarEvent(msg, SnackbarDuration.Indefinite))
                }

                BleConnectionState.DISCONNECTED -> {
                    val msg = context.getString(R.string.disconnected)
//                    _snackbarEvent.send(SnackbarEvent(msg))
//                    showSnackbar(SnackbarEvent(msg, SnackbarDuration.Indefinite))
                }

                else -> { //BleConnectionState.CONNECTED
//                    _snackbarEvent.send(SnackbarEvent(""))
                    //showSnackbar(SnackbarEvent("")) //Send empty msg to dismiss snackbar
                    _biometricAuthEvent.send("")
                }
            }
        }
    }

    private fun showSnackbar(event: SnackbarEvent) {
        val (msg, duration) = event
        Log.e("HuyTest", "showSnackbar $msg Start")

        Log.e("HuyTest", "showSnackbar End")
    }


    fun testNavigate() {
        viewModelScope.launch {
            Log.e("HuyTest", "testNavigate Start")
            _navigationEvent.send(
                NavigationEvent(
                    Screen.RegisterPasswordScreen.route,
                    Screen.UnlockScreen.route,
                    true
                )
            )
            Log.e("HuyTest", "testNavigate End")
        }
    }

    fun testNavigate2() {
        viewModelScope.launch {
            Log.e("HuyTest", "testNavigate Start")
            _navigationEvent.send(
                NavigationEvent(
                    Screen.UnlockScreen.route,
                    Screen.RegisterPasswordScreen.route,
                    true
                )
            )
            Log.e("HuyTest", "testNavigate End")
        }
    }
}