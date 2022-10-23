package sg.com.trekstorageauthentication.presentation.screen.recovery.state

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.ui.focus.FocusManager
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import sg.com.trekstorageauthentication.R
import sg.com.trekstorageauthentication.presentation.component.textfield.PasswordTextFieldState
import sg.com.trekstorageauthentication.presentation.navigation.Screen
import sg.com.trekstorageauthentication.presentation.screen.recovery.RecoveryViewModel
import sg.com.trekstorageauthentication.service.ble.BleResponseType

class RecoveryScreenStateHolder(
    private val navController: NavHostController,
    private val context: Context,
    private val viewModel: RecoveryViewModel,
    private val coroutineScope: CoroutineScope,
    private val focusManager: FocusManager,
    private val _pinState: MutableState<PasswordTextFieldState>,
) {
    val pinState: MutableState<PasswordTextFieldState>
        get() = _pinState

    init {
        registerDataResponseEvent()
    }

    fun setPinStateValue(value: String) {
        _pinState.value = _pinState.value.copy(input = value)
    }

    fun clearFocus() {
        focusManager.clearFocus()
    }

    fun login() {
        if (verifyPin()) viewModel.login(_pinState.value.input)
    }

    private fun registerDataResponseEvent() {
        coroutineScope.launch {
            viewModel.getDataResponseEvent().collect {
                val (type, _) = it

                when (type) {
                    BleResponseType.LOG_IN_SUCCESS -> {
                        val msg = context.getString(R.string.recovery_successful)
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()

                        viewModel.saveStoredPin(context, _pinState.value.input)

                        navController.navigate(Screen.AuthSuccessScreen.route) {
                            popUpTo(Screen.RecoveryScreen.route) { inclusive = true }
                        }
                    }

                    BleResponseType.LOG_IN_FAIL -> {
                        val msg = context.getString(R.string.error_recovery_pin_incorrect)
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    }

                    else -> Unit
                }
            }
        }
    }

    private fun verifyPin(): Boolean {
        val pin = _pinState.value.input
        return try {
            if (pin.isEmpty())
                throw RuntimeException(context.getString(R.string.error_field_empty))

            if (_pinState.value.isError)
                _pinState.value = PasswordTextFieldState(input = pin)

            true
        } catch (e: RuntimeException) {
            _pinState.value = _pinState.value.copy(
                isError = true,
                errorLabel = e.message!!
            )

            false
        }
    }
}