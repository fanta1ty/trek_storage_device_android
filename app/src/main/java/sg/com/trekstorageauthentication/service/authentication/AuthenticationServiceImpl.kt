package sg.com.trekstorageauthentication.service.authentication

import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import sg.com.trekstorageauthentication.R

class AuthenticationServiceImpl(private val context: Context) : AuthenticationService {
    override fun authenticate(
        onBiometricAuthenticationSuccess: () -> Unit,
        onBiometricAuthenticationFail: () -> Unit
    ) {
        if (!isBiometricAuthenticationReady()) {
            if (isPasscodeAuthenticationReady()) {
                onBiometricAuthenticationFail()
            } else {
                val msg = context.getString(R.string.settings_authentication_disabled)
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            }
            return
        }

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(context.getString(R.string.biometric_authentication_title))
            .setDescription(context.getString(R.string.authentication_description))
            .setNegativeButtonText(context.getString(R.string.use_passcode))

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
            context.getString(R.string.passcode_authentication_title),
            context.getString(R.string.authentication_description)
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