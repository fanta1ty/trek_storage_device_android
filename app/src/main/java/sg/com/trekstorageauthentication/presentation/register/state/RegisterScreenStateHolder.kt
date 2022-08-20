package sg.com.trekstorageauthentication.presentation.register.state

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager

class RegisterScreenStateHolder(
    private val focusManager: FocusManager,
    private val _currentPassword: MutableState<String>,
    private val _newPassword: MutableState<String>
) {
    val currentPassword: State<String>
        get() = _currentPassword

    val newPassword: State<String>
        get() = _newPassword

    fun setCurrentPassword(value: String) {
        _currentPassword.value = value
    }

    fun setNewPassword(value: String) {
        _newPassword.value = value
    }

    fun moveFocusDown() {
        focusManager.moveFocus(FocusDirection.Down)
    }

    fun clearFocus() {
        focusManager.clearFocus()
    }
}