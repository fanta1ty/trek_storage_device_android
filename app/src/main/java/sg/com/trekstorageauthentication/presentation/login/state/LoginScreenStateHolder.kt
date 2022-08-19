package sg.com.trekstorageauthentication.presentation.login.state

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import sg.com.trekstorageauthentication.presentation.login.LoginViewModel
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class LoginScreenStateHolder(
    private val context: Context,
    val viewModel: LoginViewModel,
    private val coroutineScope: CoroutineScope,
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

        coroutineScope.launch {
            when (awaitBiometricAuthenticationResult()) {
                true -> {
                    //Show success snack bar
                }

                false -> {
                    //Vibrate 2 times
                    //Show error snack bar
                }
            }
        }
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

    private suspend fun awaitBiometricAuthenticationResult(): Boolean {
        return suspendCoroutine { continuation ->
            val promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("")
                .setSubtitle("")
                .setNegativeButtonText("")
                .build()

            val executor = ContextCompat.getMainExecutor(context)

            BiometricPrompt(
                context as FragmentActivity,
                executor,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        continuation.resume(true)
                    }

                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)
                        continuation.resume(false)
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        continuation.resume(false)
                    }
                }).apply { authenticate(promptInfo) }
        }
    }
}