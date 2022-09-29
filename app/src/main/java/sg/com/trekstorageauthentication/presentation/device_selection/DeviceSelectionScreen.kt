package sg.com.trekstorageauthentication.presentation.device_selection

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import sg.com.trekstorageauthentication.presentation.MainViewModel

@Composable
fun DeviceSelectionScreen() {
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {}
                Lifecycle.Event.ON_STOP -> {}
                else -> Unit
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    Column() {
        LazyColumn(content = {
            items(2) {

            }
        })
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun rememberDeviceSelectionScreenStateHolder(
    viewModel: MainViewModel = hiltViewModel(),
    multiplePermissionsState: MultiplePermissionsState = rememberMultiplePermissionsState(
        permissions = viewModel.getRequiredPermissions(),
        onPermissionsResult = { viewModel.apply { connectBle(getPermissionResult(it)) } }
    )
) {

}