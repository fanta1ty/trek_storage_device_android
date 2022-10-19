package sg.com.trekstorageauthentication.presentation.component

import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import sg.com.trekstorageauthentication.presentation.state.SnackbarEvent

@Composable
fun MainEventRegisterHandler(navigationEvent: suspend () -> Unit) {
    LaunchedEffect(key1 = true) { launch { navigationEvent() } }
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