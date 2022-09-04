package sg.com.trekstorageauthentication.presentation.main.component

import android.util.Log
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import sg.com.trekstorageauthentication.R
import sg.com.trekstorageauthentication.presentation.main.state.MainStateHolder
import sg.com.trekstorageauthentication.presentation.ui.common.Snackbar

@Composable
fun MainSnackbar(msg: String, stateHolder: MainStateHolder) {
    Log.e("HuyTest", "Show Snackbar $msg//")

    when (msg) {
        stringResource(R.string.connecting) -> {
            Snackbar(msg, backgroundColor = MaterialTheme.colors.primary)
        }

        else -> { //stringResource(R.string.disconnected)
            Snackbar(
                msg,
                backgroundColor = Color.Red,
                actionLabel = stringResource(R.string.reconnect),
                actionColor = Color.Yellow,
                actionEvent = stateHolder::connectBle
            )
        }
    }
}