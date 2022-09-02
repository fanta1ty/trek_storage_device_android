package sg.com.trekstorageauthentication.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import sg.com.trekstorageauthentication.presentation.main.state.MainState
import sg.com.trekstorageauthentication.service.ble.BleService
import sg.com.trekstorageauthentication.service.permission.PermissionService
import sg.com.trekstorageauthentication.service.permission.StoragePermissionServiceImpl
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val bleService: BleService
) : ViewModel(), PermissionService by StoragePermissionServiceImpl() {

    private val _mainState = mutableStateOf(MainState())
    val mainState: State<MainState>
        get() = _mainState

    fun connectBle(permissionResult: Boolean) {
        if (!permissionResult) {
            _mainState.value = _mainState.value.copy(isPermissionsGranted = false)
            return
        } else {
            //Permission denied dialog is still showing
            //after user has granted permissions in the Settings
            if (!_mainState.value.isPermissionsGranted)
                resetMainState()
        }

//        if (!bleService.isLocationServiceEnabled()) {
//            setLocationServiceEnabled(false)
//            return
//        }

//        if (shouldScan) {
//            viewModelScope.launch {
//                bleService.connect()
//            }
//        }
    }

    fun resetMainState() {
        _mainState.value = MainState()
    }
}