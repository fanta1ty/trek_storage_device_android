package sg.com.trekstorageauthentication.service.authentication

import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

class AuthenticationServiceImpl(private val context: Context) : AuthenticationService {
    override fun authenticate(
        onBiometricAuthenticationSuccess: () -> Unit,
        onBiometricAuthenticationFail: () -> Unit
    ) {
        if (!isBiometricAuthenticationReady()) {
            if (isPasscodeAuthenticationReady()) {
                onBiometricAuthenticationFail()
            } else {
                Toast.makeText(
                    context,
                    "You haven't set up any security measures on your phone's settings.",
                    Toast.LENGTH_SHORT
                ).show()
            }

            return
        }

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Authentication")
            .setDescription("Please authenticate")
            .setNegativeButtonText("Use passcode")

        val executor = ContextCompat.getMainExecutor(context)

        BiometricPrompt(
            context as FragmentActivity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    onBiometricAuthenticationSuccess()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                        onBiometricAuthenticationFail()
                    }
                }
            }).apply { authenticate(promptInfo.build()) }
    }

    @Suppress("DEPRECATION")
    override fun getPasscodeAuthenticationIntent(): Intent {
        val keyguardManager = context.getSystemService(KeyguardManager::class.java)
        return keyguardManager.createConfirmDeviceCredentialIntent(
            "Passcode Authentication",
            "Please enter passcode"
        )
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

    private fun isPasscodeAuthenticationReady(): Boolean {
        val keyguardManager = context.getSystemService(KeyguardManager::class.java)
        return keyguardManager.isDeviceSecure
    }
}