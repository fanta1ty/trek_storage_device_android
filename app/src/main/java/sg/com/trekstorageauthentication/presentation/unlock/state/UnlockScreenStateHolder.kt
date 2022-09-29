package sg.com.trekstorageauthentication.presentation.unlock.state

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import sg.com.trekstorageauthentication.R
import sg.com.trekstorageauthentication.presentation.MainViewModel
import sg.com.trekstorageauthentication.presentation.ui.navigation.Screen

class UnlockScreenStateHolder(
    private val context: Context,
    private val viewModel: MainViewModel,
) {
    fun authenticate() {
        val isBiometricAuthenticationReady = isBiometricAuthenticationReady()
        if (!isBiometricAuthenticationReady) {
            viewModel.checkAlreadyLogIn()
            return
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
                    viewModel.checkAlreadyLogIn()
                }
            }).apply { authenticate(promptInfo) }
    }

    fun navigateResetScreen() {
        viewModel.navigate(Screen.ResetScreen.route)
    }

    suspend fun registerBiometricAuthEvent() {
        viewModel.biometricAuthEvent.collect { authenticate() }
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