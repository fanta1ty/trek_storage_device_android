package sg.com.trekstorageauthentication.presentation.login.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.res.stringResource
import sg.com.trekstorageauthentication.R
import sg.com.trekstorageauthentication.presentation.login.state.BiometricAuthenticationReadyState
import sg.com.trekstorageauthentication.presentation.ui.common.Dialog

@Composable
fun BiometricAuthenticationDisabledDialog(
    state: State<BiometricAuthenticationReadyState>,
    onNegativeEvent: () -> Unit
) {
    when (state.value) {
        BiometricAuthenticationReadyState.NOT_ENROLLED -> {
            Dialog(
                title = stringResource(R.string.dialog_biometric_authentication_ready_title),
                content = stringResource(R.string.dialog_biometric_authentication_not_enrolled),
                onNegativeClickEvent = { onNegativeEvent() }
            )
        }

        BiometricAuthenticationReadyState.FAILED -> {
            Dialog(
                title = stringResource(R.string.dialog_biometric_authentication_ready_title),
                content = stringResource(R.string.dialog_biometric_authentication_failed),
                onNegativeClickEvent = { onNegativeEvent() }
            )
        }

        else -> Unit
    }
}