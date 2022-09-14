package sg.com.trekstorageauthentication.presentation.main.component

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import sg.com.trekstorageauthentication.R
import sg.com.trekstorageauthentication.presentation.ui.common.Snackbar

@Composable
fun MainSnackbar(
    msg: String,
    connectBle: () -> Unit
) {
    when (msg) {
        stringResource(R.string.connecting) -> {
            Snackbar(msg, backgroundColor = MaterialTheme.colors.primary)
        }

        stringResource(R.string.disconnected) -> {
            Snackbar(
                msg,
                backgroundColor = Color.Red,
                actionLabel = stringResource(R.string.reconnect),
                actionColor = Color.Yellow,
                actionEvent = connectBle
            )
        }

        stringResource(R.string.register_password_success),
        stringResource(R.string.unlock_storage_success),
        stringResource(R.string.reset_password_success) -> {
            Snackbar(msg, backgroundColor = Color.Green)
        }

        stringResource(R.string.register_password_fail),
        stringResource(R.string.unlock_storage_fail),
        stringResource(R.string.reset_password_fail),
        stringResource(R.string.no_trek_devices_found) -> {
            Snackbar(msg, backgroundColor = Color.Red)
        }

        else -> Unit
    }
}