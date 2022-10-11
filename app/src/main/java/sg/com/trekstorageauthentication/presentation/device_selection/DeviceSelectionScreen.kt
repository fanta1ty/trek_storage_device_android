@file:OptIn(ExperimentalPermissionsApi::class)

package sg.com.trekstorageauthentication.presentation.device_selection

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import sg.com.trekstorageauthentication.presentation.device_selection.component.DeviceItem
import sg.com.trekstorageauthentication.presentation.device_selection.component.DeviceSelectionDialog
import sg.com.trekstorageauthentication.presentation.device_selection.state.DeviceSelectionScreenStateHolder

@SuppressLint("MissingPermission")
@Composable
fun DeviceSelectionScreen() {
    val stateHolder = rememberDeviceSelectionScreenStateHolder()
    val isScanningState = stateHolder.viewModel.getIsScanningState().collectAsState()
    val trekDevice = stateHolder.viewModel.trekDevice

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(color = MaterialTheme.colors.primary)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                stringResource(R.string.scanning_devices),
                style = MaterialTheme.typography.h4,
                color = MaterialTheme.colors.onPrimary
            )

            Row(
                modifier = Modifier.fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isScanningState.value) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .width(24.dp)
                            .height(24.dp),
                        color = MaterialTheme.colors.onPrimary,
                        strokeWidth = 2.dp
                    )

                    Spacer(modifier = Modifier.width(16.dp))
                }

                TextButton(onClick = stateHolder::toggleScanningOnOff) {
                    val stringId = if (isScanningState.value) R.string.stop else R.string.scan
                    Text(
                        stringResource(stringId),
                        style = MaterialTheme.typography.h4,
                        color = MaterialTheme.colors.onPrimary
                    )
                }
            }
        }

        LazyColumn(content = {
            items(trekDevice.size) {
                DeviceItem(
                    trekDevice[it].name.takeIf { name -> !name.isNullOrEmpty() } ?: "N/A"
                )
            }
        })
    }

    DeviceSelectionDialog(
        state = stateHolder.viewModel.dialogState.collectAsState(),
        onPermissionPositiveEvent = stateHolder::navigateToAppPermissionSettings,
        onLocationDisabledPositiveEvent = stateHolder::navigateToLocationServiceSettings,
        onBluetoothDisabledPositiveEvent = stateHolder.viewModel::dismissDialog
    )
}

@Composable
private fun rememberDeviceSelectionScreenStateHolder(
    context: Context = LocalContext.current,
    viewModel: DeviceSelectionViewModel = hiltViewModel(),
    multiplePermissionsState: MultiplePermissionsState = rememberMultiplePermissionsState(
        permissions = viewModel.getRequiredPermissions(),
        onPermissionsResult = { viewModel.apply { startScan(getPermissionResult(it)) } }
    )
): DeviceSelectionScreenStateHolder {
    return remember {
        DeviceSelectionScreenStateHolder(context, viewModel, multiplePermissionsState)
    }
}