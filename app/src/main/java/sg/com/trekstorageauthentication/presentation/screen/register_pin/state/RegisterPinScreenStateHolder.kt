package sg.com.trekstorageauthentication.presentation.screen.register_pin.state

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import sg.com.trekstorageauthentication.R
import sg.com.trekstorageauthentication.presentation.component.textfield.PasswordTextFieldState
import sg.com.trekstorageauthentication.presentation.navigation.Screen
import sg.com.trekstorageauthentication.presentation.screen.register_pin.RegisterPinViewModel
import sg.com.trekstorageauthentication.service.ble.BleResponseType

class RegisterPinScreenStateHolder(
    private val navController: NavHostController?,
    private val context: Context,
    private val focusManager: FocusManager,
    private val viewModel: RegisterPinViewModel,
    private val coroutineScope: CoroutineScope,
    private val _pinState: MutableState<PasswordTextFieldState>,
    private val _confirmPinState: MutableState<PasswordTextFieldState>
) {
    val pinState: State<PasswordTextFieldState>
        get() = _pinState

    val confirmPinState: State<PasswordTextFieldState>
        get() = _confirmPinState

    init {
        registerDataResponseEvent()
    }

    fun setPinStateValue(value: String) {
        _pinState.value = _pinState.value.copy(input = value)
    }

    fun setConfirmPinStateValue(value: String) {
        _confirmPinState.value = _confirmPinState.value.copy(input = value)
    }

    fun moveFocusDown() {
        focusManager.moveFocus(FocusDirection.Down)
    }

    fun clearFocus() {
        focusManager.clearFocus()
    }

    fun save() {
        if (verifyPin() && verifyConfirmPin()) {
            clearFocus()
            viewModel.registerPin(_pinState.value.input)
        }
    }

    private fun registerDataResponseEvent() {
        coroutineScope.launch {
            viewModel.getDataResponseEvent().collect {
                val (type, _) = it

                when (type) {
                    BleResponseType.REGISTER_PIN_SUCCESS -> {
                        viewModel.saveStoredPin(context, _pinState.value.input)

                        navController?.navigate(Screen.AuthSuccessScreen.route) {
                            popUpTo(Screen.RegisterPinScreen.route) { inclusive = true }
                        }
                    }

                    BleResponseType.REGISTER_PIN_FAIL -> {
                        val msg = context.getString(R.string.error_register_pin_failed)
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

            if (pin.length < 8)
                throw RuntimeException(context.getString(R.string.error_pin_invalid_length))

            if (pin.contains(' '))
                throw RuntimeException(context.getString(R.string.error_pin_invalid_space))

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

    private fun verifyConfirmPin(): Boolean {
        val pin = _pinState.value.input
        val confirmPin = _confirmPinState.value.input

        return try {
            if (confirmPin.isEmpty())
                throw RuntimeException(context.getString(R.string.error_field_empty))

            if (confirmPin != pin)
                throw RuntimeException(context.getString(R.string.error_confirm_pin_not_match))

            if (_confirmPinState.value.isError)
                _confirmPinState.value = PasswordTextFieldState(input = confirmPin)

            true
        } catch (e: RuntimeException) {
            _confirmPinState.value = _confirmPinState.value.copy(
                isError = true,
                errorLabel = e.message!!
            )

            false
        }
    }
}