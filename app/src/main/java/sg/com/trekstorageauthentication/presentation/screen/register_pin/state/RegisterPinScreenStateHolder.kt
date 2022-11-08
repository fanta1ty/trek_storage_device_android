package sg.com.trekstorageauthentication.presentation.screen.register_pin.state

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import sg.com.trekstorageauthentication.R
import sg.com.trekstorageauthentication.presentation.component.textfield.PasswordTextFieldState
import sg.com.trekstorageauthentication.presentation.navigation.Screen
import sg.com.trekstorageauthentication.presentation.screen.register_pin.RegisterPinViewModel
import sg.com.trekstorageauthentication.service.ble.BleResponseType
import sg.com.trekstorageauthentication.util.RandomUtil
import java.util.*

class RegisterPinScreenStateHolder(
    private val isRegister: Boolean,
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
        setUpDevice()
    }

    private fun setUpDevice() {
        // Auto generate pin and register it
        coroutineScope.launch {
            // Add delay so that data response event is registered properly
            delay(2000)
            val pin = RandomUtil.randomNumericString(8)
            viewModel.saveStoredPin(context, pin)
            viewModel.registerPin(pin)
        }
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

            if (isRegister)
                viewModel.registerPin(_pinState.value.input)
            else
                viewModel.changePin(_pinState.value.input)
        }
    }

    private fun registerDataResponseEvent() {
        coroutineScope.launch {
            viewModel.getDataResponseEvent().collect {
                val (type, _) = it

                when (type) {
                    BleResponseType.REGISTER_PIN_SUCCESS,
                    BleResponseType.CHANGE_PIN_SUCCESS -> {
//                        viewModel.saveStoredPin(context, _pinState.value.input)
//
//                        val msg = if (isRegister)
//                            context.getString(R.string.error_register_pin_successful)
//                        else
//                            context.getString(R.string.error_reset_pin_successful)

//                        val msg = context.getString(R.string.device_setup_successful)
//
//                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()

                        val currentRoute = navController?.currentBackStackEntry?.destination?.route
                            ?: Screen.RegisterPinScreen.route

                        navController?.navigate(Screen.AuthSuccessScreen.withArgs(true)) {
                            popUpTo(currentRoute) { inclusive = true }
                        }

//                        navController?.navigate(Screen.AuthSuccessScreen.route) {
//                            popUpTo(currentRoute) { inclusive = true }
//                        }
                    }

                    BleResponseType.REGISTER_PIN_FAIL,
                    BleResponseType.CHANGE_PIN_FAIL -> {
//                        val msg = if (isRegister)
//                            context.getString(R.string.error_register_pin_failed)
//                        else
//                            context.getString(R.string.error_reset_pin_failed)

                        val msg = context.getString(R.string.device_setup_failed)

                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()

                        navController?.popBackStack()
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