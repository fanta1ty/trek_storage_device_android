package sg.com.trekstorageauthentication.presentation.login.state

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import sg.com.trekstorageauthentication.R
import sg.com.trekstorageauthentication.presentation.login.LoginViewModel

class LoginScreenStateHolder(
    private val context: Context,
    val viewModel: LoginViewModel,
    private val _password: MutableState<String>
) {
    val password: State<String>
        get() = _password

    fun setPassword(value: String) {
        _password.value = value
    }

    fun passwordAuthenticate() {
        //Password must be at least 8 characters
    }

    fun biometricAuthenticate() {
        val isBiometricAuthenticationReady = canPerformBiometricAuthentication()
        if (!isBiometricAuthenticationReady) return

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
                    //TODO: Show success snack bar
                }
            }).apply { authenticate(promptInfo) }
    }

    fun dismissBiometricAuthenticationDisabledDialog() {
        viewModel.setBiometricAuthenticationReadyState(BiometricAuthenticationReadyState.READY)
    }

    private fun canPerformBiometricAuthentication(): Boolean {
        val biometricManager = BiometricManager.from(context)
        return when (biometricManager.canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_STRONG or
                    BiometricManager.Authenticators.BIOMETRIC_WEAK
        )) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                viewModel.setBiometricAuthenticationReadyState(
                    BiometricAuthenticationReadyState.READY
                )
                true
            }

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                viewModel.setBiometricAuthenticationReadyState(
                    BiometricAuthenticationReadyState.NOT_ENROLLED
                )
                false
            }

            else -> {
                viewModel.setBiometricAuthenticationReadyState(
                    BiometricAuthenticationReadyState.FAILED
                )
                false
            }
        }
    }
}