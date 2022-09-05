package sg.com.trekstorageauthentication.presentation.register_password.state

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import sg.com.trekstorageauthentication.R
import sg.com.trekstorageauthentication.presentation.ui.common.textfield.PasswordTextFieldState

class RegisterPasswordScreenStateHolder(
    private val context: Context,
    private val focusManager: FocusManager,
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
        val passwordInput = _passwordState.value.input
        val confirmPasswordInput = _confirmPasswordState.value.input

        if (verifyPassword(passwordInput) &&
            verifyConfirmPassword(confirmPasswordInput, passwordInput)
        ) {
            //TODO: Save password to local
            //TODO: send password to PC
            TODO()
        }
    }

    private fun verifyPassword(password: String): Boolean {
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

    private fun verifyConfirmPassword(confirmPassword: String, password: String): Boolean {
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