package sg.com.trekstorageauthentication.presentation.login

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import sg.com.trekstorageauthentication.presentation.login.state.BiometricAuthenticationReadyState

class LoginViewModel : ViewModel() {
    private val _biometricAuthenticationReadyState = mutableStateOf(
        BiometricAuthenticationReadyState.READY
    )
    val biometricAuthenticationReadyState: State<BiometricAuthenticationReadyState>
        get() = _biometricAuthenticationReadyState

    fun setBiometricAuthenticationReadyState(state: BiometricAuthenticationReadyState) {
        _biometricAuthenticationReadyState.value = state
    }
}