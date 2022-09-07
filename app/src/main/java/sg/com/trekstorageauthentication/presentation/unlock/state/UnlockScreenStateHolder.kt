package sg.com.trekstorageauthentication.presentation.unlock.state

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.ui.focus.FocusManager
import sg.com.trekstorageauthentication.R
import sg.com.trekstorageauthentication.presentation.MainViewModel
import sg.com.trekstorageauthentication.presentation.ui.common.textfield.PasswordTextFieldState

class UnlockScreenStateHolder(
    private val context: Context,
    private val focusManager: FocusManager,
    private val viewModel: MainViewModel,
    private val _passwordState: MutableState<PasswordTextFieldState>
) {
    val passwordState: State<PasswordTextFieldState>
        get() = _passwordState

    fun setPasswordState(value: String) {
        _passwordState.value = _passwordState.value.copy(input = value)
    }

    fun clearFocus() {
        focusManager.clearFocus()
    }

    fun authenticate() {
        if (isPasswordValid()) {
            viewModel.unlockTrekStorage(_passwordState.value.input)
        }
    }

    private fun isPasswordValid(): Boolean {
        val password = _passwordState.value.input

        return try {
            if (password.isEmpty())
                throw RuntimeException(context.getString(R.string.error_field_empty))

            _passwordState.value = PasswordTextFieldState(input = password)

            true
        } catch (e: RuntimeException) {
            _passwordState.value = _passwordState.value.copy(
                isError = true, errorLabel = e.message!!
            )

            false
        }
    }
}