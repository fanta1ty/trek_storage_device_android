package sg.com.trekstorageauthentication.service.authentication

import android.content.Intent

interface AuthenticationService {
    fun authenticate(
        onBiometricAuthenticationSuccess: () -> Unit,
        onBiometricAuthenticationFail: () -> Unit
    )

    fun getPasscodeAuthenticationIntent(): Intent
}