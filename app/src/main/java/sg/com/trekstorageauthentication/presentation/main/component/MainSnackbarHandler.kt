package sg.com.trekstorageauthentication.presentation.main.component

import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import sg.com.trekstorageauthentication.presentation.main.state.SnackbarEvent

@Composable
fun MainSnackbarHandler(
    snackbarEventState: State<SnackbarEvent>,
    scaffoldState: ScaffoldState
) {
    val snackbarEvent = snackbarEventState.value
    LaunchedEffect(key1 = snackbarEvent) {
        val msg = snackbarEvent.msg
        val duration = snackbarEvent.duration
        if (msg.isNotEmpty()) {
            scaffoldState.snackbarHostState.showSnackbar(msg, duration = duration)
        }
    }
}