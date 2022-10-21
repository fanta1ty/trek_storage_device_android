package sg.com.trekstorageauthentication.presentation

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import sg.com.trekstorageauthentication.common.Constants
import sg.com.trekstorageauthentication.presentation.state.MainState
import sg.com.trekstorageauthentication.presentation.state.NavigationEvent
import sg.com.trekstorageauthentication.presentation.state.SnackbarEvent
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

    private val _snackbarEvent = Channel<SnackbarEvent>()
    val snackbarEvent = _snackbarEvent.receiveAsFlow()

    private val _biometricAuthEvent = MutableSharedFlow<Unit>()
    val biometricAuthEvent = _biometricAuthEvent.asSharedFlow()

    private val _navigationEvent = Channel<NavigationEvent>()
    val navigationEvent = _navigationEvent.receiveAsFlow()

    fun checkAlreadyLogIn() {
        bleService.read(Constants.NOTIFICATION_CHARACTERISTIC_UUID)
    }

    fun registerPassword(password: String) {
        showLoading()
        val uniqueId = generateUniqueIdentifier()
        viewModelScope.launch { saveStoredPin(context, uniqueId) }
        val request = "$password\r\n$uniqueId".toByteArray()
        bleService.write(Constants.REGISTER_PIN_CHARACTERISTIC_UUID, request)
    }

    fun navigate(
        route: String = "",
        popUpToRoute: String = "",
        isInclusive: Boolean = true
    ) {
        viewModelScope.launch {
            _navigationEvent.send(NavigationEvent(route, popUpToRoute, isInclusive))
        }
    }

    private fun showLoading() {
        _mainState.value = _mainState.value.copy(isLoading = true)
    }

    override fun onCleared() {
        bleService.close()
        super.onCleared()
    }
}