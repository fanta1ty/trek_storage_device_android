package sg.com.trekstorageauthentication.presentation.main.component

import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import sg.com.trekstorageauthentication.presentation.main.state.SnackbarEvent

@Composable
fun MainEventRegisterHandler(navigationEvent: suspend () -> Unit) {
    LaunchedEffect(key1 = true) { launch { navigationEvent() } }
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
    snackbarEvent: Flow<SnackbarEvent>,
    scaffoldState: ScaffoldState
) {
    LaunchedEffect(key1 = true) {
        var job: Job? = null
        snackbarEvent.collect { (msg, duration) ->
            job?.cancel()
            if (msg.isNotEmpty()) {
                job = launch {
                    scaffoldState.snackbarHostState.showSnackbar(msg, duration = duration)
                }
            }
        }
    }
}