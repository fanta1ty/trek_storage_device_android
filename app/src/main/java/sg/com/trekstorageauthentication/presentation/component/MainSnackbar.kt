package sg.com.trekstorageauthentication.presentation.component

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import sg.com.trekstorageauthentication.R
import sg.com.trekstorageauthentication.presentation.ui.common.Snackbar
import sg.com.trekstorageauthentication.presentation.ui.theme.GreenA400

@Composable
fun MainSnackbar(msg: String) {
    when (msg) {
        stringResource(R.string.connecting) -> {
            Snackbar(msg, backgroundColor = MaterialTheme.colors.primary)
        }

        stringResource(R.string.register_password_success),
        stringResource(R.string.unlock_storage_success) -> {
            Snackbar(msg, backgroundColor = GreenA400)
        }

        stringResource(R.string.register_password_fail),
        stringResource(R.string.unlock_storage_fail),
        stringResource(R.string.reset_settings_fail),
        stringResource(R.string.no_trek_devices_found) -> {
            Snackbar(msg, backgroundColor = Color.Red)
        }

        else -> Unit
    }
}