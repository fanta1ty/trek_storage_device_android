package sg.com.trekstorageauthentication.presentation.main.component

import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import kotlinx.coroutines.launch
import sg.com.trekstorageauthentication.presentation.main.state.SnackbarEvent

@Composable
fun MainEventRegisterHandler(
    biometricAuthEvent: suspend () -> Unit,
    navigationEvent: suspend () -> Unit,
) {
    LaunchedEffect(key1 = true) {
        launch { biometricAuthEvent() }
        launch { navigationEvent() }
    }
}

@Composable
fun MainLifecycleHandler(connectBle: () -> Unit) {
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                connectBle()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }
}

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
            //launch { scaffoldState.snackbarHostState.showSnackbar(msg, duration = duration) }
            scaffoldState.snackbarHostState.showSnackbar(msg, duration = duration)
        }
    }
}