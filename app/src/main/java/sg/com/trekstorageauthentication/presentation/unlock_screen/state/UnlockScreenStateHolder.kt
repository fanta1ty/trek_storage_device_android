package sg.com.trekstorageauthentication.presentation.unlock_screen.state

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.ui.focus.FocusManager
import sg.com.trekstorageauthentication.R
import sg.com.trekstorageauthentication.presentation.ui.common.textfield.PasswordTextFieldState

class UnlockScreenStateHolder(
    private val context: Context,
    private val focusManager: FocusManager,
    private val _passwordState: MutableState<PasswordTextFieldState>
) {
    val passwordState: State<PasswordTextFieldState>
        get() = _passwordState

    fun setPasswordState(value: String) {
        _passwordState.value = _passwordState.value.copy(input = value)
    }

    fun authenticate() {
        if (isPasswordValid()) {
            TODO("Unlock device")
        }
    }

    fun clearFocus() {
        focusManager.clearFocus()
    }

    private fun isPasswordValid(): Boolean {
        val passwordInput = _passwordState.value.input

        return try {
            if (passwordInput.isEmpty())
                throw RuntimeException(context.getString(R.string.error_field_empty))

            //TODO: Check password not match

            _passwordState.value = PasswordTextFieldState(input = passwordInput)

            true
        } catch (e: RuntimeException) {
            _passwordState.value = _passwordState.value.copy(
                isError = true,
                errorLabel = e.message!!
            )

            false
        }
    }
}