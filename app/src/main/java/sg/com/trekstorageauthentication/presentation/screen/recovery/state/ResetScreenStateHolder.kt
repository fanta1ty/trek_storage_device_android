package sg.com.trekstorageauthentication.presentation.screen.recovery.state

import androidx.compose.runtime.MutableState
import androidx.compose.ui.focus.FocusManager
import sg.com.trekstorageauthentication.presentation.component.textfield.PasswordTextFieldState

class ResetScreenStateHolder(
    private val focusManager: FocusManager,
    private val _passwordState: MutableState<PasswordTextFieldState>,
) {
    val passwordState: MutableState<PasswordTextFieldState>
        get() = _passwordState

    fun setPasswordStateValue(value: String) {
        _passwordState.value = _passwordState.value.copy(input = value)
    }

    fun clearFocus() {
        focusManager.clearFocus()
    }

    fun showConfirmResetDialog() {
    }

    fun reset() {

    }
}