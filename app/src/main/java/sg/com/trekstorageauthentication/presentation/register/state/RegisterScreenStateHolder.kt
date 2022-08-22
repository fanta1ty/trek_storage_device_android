package sg.com.trekstorageauthentication.presentation.register.state

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import sg.com.trekstorageauthentication.R
import sg.com.trekstorageauthentication.presentation.ui.common.textfield.PasswordTextFieldState

class RegisterScreenStateHolder(
    private val context: Context,
    private val focusManager: FocusManager,
    private val _currentPassword: MutableState<PasswordTextFieldState>,
    private val _newPassword: MutableState<PasswordTextFieldState>,
) {
    val currentPassword: State<PasswordTextFieldState>
        get() = _currentPassword

    val newPassword: State<PasswordTextFieldState>
        get() = _newPassword

    fun setCurrentPassword(value: String) {
        _currentPassword.value = _currentPassword.value.copy(input = value)
    }

    fun setNewPassword(value: String) {
        _newPassword.value = _newPassword.value.copy(input = value)
    }

    fun moveFocusDown() {
        //Set TextField enabled = false if that view shouldn't be allowed to receive focus
        focusManager.moveFocus(FocusDirection.Down)
    }

    fun clearFocus() {
        focusManager.clearFocus()
    }

    fun save() {
        val currentPasswordInput = _currentPassword.value.input
        val newPasswordInput = _newPassword.value.input

        try {
            if (currentPasswordInput.isEmpty())
                throw RuntimeException(context.getString(R.string.error_field_empty))

            if (_currentPassword.value.isError)
                _currentPassword.value = PasswordTextFieldState(input = currentPasswordInput)
        } catch (e: RuntimeException) {
            _currentPassword.value = _currentPassword.value.copy(
                isError = true,
                errorLabel = e.message!!
            )
            return
        }

        try {
            if (newPasswordInput.isEmpty())
                throw RuntimeException(context.getString(R.string.error_field_empty))

            if (newPasswordInput.length < 8)
                throw RuntimeException(
                    context.getString(R.string.error_password_invalid_length)
                )

            if (_newPassword.value.isError)
                _newPassword.value = PasswordTextFieldState(input = newPasswordInput)
        } catch (e: RuntimeException) {
            _newPassword.value = _newPassword.value.copy(
                isError = true,
                errorLabel = e.message!!
            )
            return
        }
    }
}