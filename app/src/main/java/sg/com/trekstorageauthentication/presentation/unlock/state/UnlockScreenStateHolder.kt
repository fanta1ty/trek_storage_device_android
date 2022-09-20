package sg.com.trekstorageauthentication.presentation.unlock.state

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.ui.focus.FocusManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
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
            clearFocus()
            viewModel.logIn(_passwordState.value.input)
        }
    }

    fun resetSettings() {
        //Show reset settings confirm dialog
    }

    suspend fun registerBiometricAuthEvent() {
        viewModel.biometricAuthEvent.collect {
            val isBiometricAuthenticationReady = isBiometricAuthenticationReady()
            if (!isBiometricAuthenticationReady) {
                viewModel.getPasswordStatus()
                return@collect
            }

            val promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle(context.getString(R.string.dialog_biometric_authentication_title))
                .setDescription(context.getString(R.string.dialog_biometric_authentication_description))
                .setNegativeButtonText(context.getString(android.R.string.cancel))
                .build()

            val executor = ContextCompat.getMainExecutor(context)

            BiometricPrompt(
                context as FragmentActivity,
                executor,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        viewModel.getPasswordStatus()
                    }
                }).apply { authenticate(promptInfo) }
        }
    }

    private fun isPasswordValid(): Boolean {
        val password = _passwordState.value.input

        return try {
            if (password.isEmpty())
                throw RuntimeException(context.getString(R.string.error_field_empty))

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

    private fun isBiometricAuthenticationReady(): Boolean {
        val biometricManager = BiometricManager.from(context)

        return when (biometricManager.canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_STRONG or
                    BiometricManager.Authenticators.BIOMETRIC_WEAK
        )) {
            BiometricManager.BIOMETRIC_SUCCESS -> true
            else -> false
        }
    }
}