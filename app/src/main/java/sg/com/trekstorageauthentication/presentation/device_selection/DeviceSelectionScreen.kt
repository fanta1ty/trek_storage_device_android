@file:OptIn(ExperimentalPermissionsApi::class)

package sg.com.trekstorageauthentication.presentation.device_selection

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import sg.com.trekstorageauthentication.R
import sg.com.trekstorageauthentication.presentation.device_selection.state.DeviceSelectionScreenStateHolder

@Composable
fun DeviceSelectionScreen() {
    val stateHolder = rememberDeviceSelectionScreenStateHolder()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),

        ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(color = MaterialTheme.colors.primary)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(stringResource(R.string.scanning_devices))
        }

        TextButton(onClick = {
            stateHolder.connectBle()
        }) {
            Text(stringResource(R.string.start_scanning))
        }

//        LazyColumn(content = {
//            items(2) {
//
//            }
//        })
    }

//    DeviceSelectionDialog(
//        state =,
//        onPermissionPositiveEvent = { },
//        onLocationDisabledPositiveEvent = { },
//        onBluetoothDisabledPositiveEvent = { }
//    )
}

@Composable
private fun rememberDeviceSelectionScreenStateHolder(
    context: Context = LocalContext.current,
    viewModel: DeviceSelectionViewModel = hiltViewModel(),
    multiplePermissionsState: MultiplePermissionsState = rememberMultiplePermissionsState(
        permissions = viewModel.getRequiredPermissions(),
        onPermissionsResult = { viewModel.apply { connectBle(getPermissionResult(it)) } }
    )
): DeviceSelectionScreenStateHolder {
    return remember {
        DeviceSelectionScreenStateHolder(context, viewModel, multiplePermissionsState)
    }
}