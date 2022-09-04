package sg.com.trekstorageauthentication.presentation.main.component

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import kotlinx.coroutines.launch
import sg.com.trekstorageauthentication.presentation.main.state.MainStateHolder

@Composable
fun MainLifecycleHandler(stateHolder: MainStateHolder) {
    val coroutineScope = rememberCoroutineScope()
    Log.e("HuyTest", "MainLifecycleHandler recompose")

    LaunchedEffect(key1 = true) {
        Log.e("HuyTest", "LaunchedEffect")

        coroutineScope.launch {
            stateHolder.getSnackbarEvent().collect { (msg, duration) ->
                Log.e("HuyTest", "LaunchedEffect received snackbar event")

//                if (msg.isEmpty()) {
//                    stateHolder.scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
//                }
//
//                stateHolder.scaffoldState.snackbarHostState.showSnackbar(msg, duration = duration)
            }
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                Log.e("HuyTest", "ON_START")
                stateHolder.connectBle()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }
}