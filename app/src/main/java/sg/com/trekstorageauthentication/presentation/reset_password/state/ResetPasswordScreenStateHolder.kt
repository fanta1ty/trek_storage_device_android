package sg.com.trekstorageauthentication.presentation.reset_password.state

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import sg.com.trekstorageauthentication.R
import sg.com.trekstorageauthentication.presentation.MainViewModel
import sg.com.trekstorageauthentication.presentation.ui.common.textfield.PasswordTextFieldState

class ResetPasswordScreenStateHolder(
    private val context: Context,
    private val focusManager: FocusManager,
    private val viewModel: MainViewModel,
    private val _currentPasswordState: MutableState<PasswordTextFieldState>,
    private val _newPasswordState: MutableState<PasswordTextFieldState>,
) {
    val currentPasswordState: State<PasswordTextFieldState>
        get() = _currentPasswordState

    val newPasswordState: State<PasswordTextFieldState>
        get() = _newPasswordState

    fun setCurrentPassword(value: String) {
        _currentPasswordState.value = _currentPasswordState.value.copy(input = value)
    }

    fun setNewPassword(value: String) {
        _newPasswordState.value = _newPasswordState.value.copy(input = value)
    }

    fun moveFocusDown() {
        focusManager.moveFocus(FocusDirection.Down)
    }

    fun clearFocus() {
        focusManager.clearFocus()
    }

    fun save() {
        val currentPasswordInput = _currentPasswordState.value.input
        val newPasswordInput = _newPasswordState.value.input

        try {
            if (currentPasswordInput.isEmpty())
                throw RuntimeException(context.getString(R.string.error_field_empty))

            if (_currentPasswordState.value.isError)
                _currentPasswordState.value = PasswordTextFieldState(input = currentPasswordInput)
        } catch (e: RuntimeException) {
            _currentPasswordState.value = _currentPasswordState.value.copy(
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

            if (_newPasswordState.value.isError)
                _newPasswordState.value = PasswordTextFieldState(input = newPasswordInput)
        } catch (e: RuntimeException) {
            _newPasswordState.value = _newPasswordState.value.copy(
                isError = true,
                errorLabel = e.message!!
            )
            return
        }
    }
}