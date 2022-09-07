package sg.com.trekstorageauthentication.presentation.register_password.state

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import sg.com.trekstorageauthentication.R
import sg.com.trekstorageauthentication.common.Constants
import sg.com.trekstorageauthentication.presentation.MainViewModel
import sg.com.trekstorageauthentication.presentation.ui.common.textfield.PasswordTextFieldState

class RegisterPasswordScreenStateHolder(
    private val context: Context,
    private val focusManager: FocusManager,
    private val viewModel: MainViewModel,
    private val _passwordState: MutableState<PasswordTextFieldState>,
    private val _confirmPasswordState: MutableState<PasswordTextFieldState>
) {
    val passwordState: State<PasswordTextFieldState>
        get() = _passwordState

    val confirmPasswordState: State<PasswordTextFieldState>
        get() = _confirmPasswordState

    fun setPasswordStateValue(value: String) {
        _passwordState.value = _passwordState.value.copy(input = value)
    }

    fun setConfirmPasswordStateValue(value: String) {
        _confirmPasswordState.value = _confirmPasswordState.value.copy(input = value)
    }

    fun moveFocusDown() {
        focusManager.moveFocus(FocusDirection.Down)
    }

    fun clearFocus() {
        focusManager.clearFocus()
    }

    fun save() {
        viewModel.testNavigate2()
//        val password = _passwordState.value.input
//        if (verifyPassword() && verifyConfirmPassword()) {
//            viewModel.sendBleData(
//                Constants.SET_PASSWORD_CHARACTERISTIC_UUID,
//                password.toByteArray()
//            )
//        }
    }

    private fun verifyPassword(): Boolean {
        val password = _passwordState.value.input
        return try {
            if (password.isEmpty())
                throw RuntimeException(context.getString(R.string.error_field_empty))

            if (password.length < 8)
                throw RuntimeException(context.getString(R.string.error_password_invalid_length))

            if (_passwordState.value.isError)
                _passwordState.value = PasswordTextFieldState(input = password)

            true
        } catch (e: RuntimeException) {
            _passwordState.value = _passwordState.value.copy(
                isError = true,
                errorLabel = e.message!!
            )

            false
        }
    }

    private fun verifyConfirmPassword(): Boolean {
        val password = _passwordState.value.input
        val confirmPassword = _confirmPasswordState.value.input

        return try {
            if (confirmPassword.isEmpty())
                throw RuntimeException(context.getString(R.string.error_field_empty))

            if (confirmPassword != password)
                throw RuntimeException(context.getString(R.string.confirm_password_not_match))

            if (_confirmPasswordState.value.isError)
                _confirmPasswordState.value = PasswordTextFieldState(input = confirmPassword)

            true
        } catch (e: RuntimeException) {
            _confirmPasswordState.value = _confirmPasswordState.value.copy(
                isError = true,
                errorLabel = e.message!!
            )

            false
        }
    }
}