@file:OptIn(ExperimentalPermissionsApi::class)

package sg.com.trekstorageauthentication.presentation.screen.device_selection

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.CoroutineScope
import sg.com.trekstorageauthentication.presentation.component.LocalNavController
import sg.com.trekstorageauthentication.presentation.screen.device_selection.component.DeviceItem
import sg.com.trekstorageauthentication.presentation.screen.device_selection.component.DeviceSelectionDialog
import sg.com.trekstorageauthentication.presentation.screen.device_selection.component.DeviceSelectionToolbar
import sg.com.trekstorageauthentication.presentation.screen.device_selection.state.DeviceSelectionScreenStateHolder

@SuppressLint("MissingPermission")
@Composable
fun DeviceSelectionScreen() {
    val stateHolder = rememberDeviceSelectionScreenStateHolder()
    val trekDevice = stateHolder.viewModel.trekDevice

    LaunchedEffect(true) { stateHolder.toggleScanningOnOff() }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        DeviceSelectionToolbar(
            isScanningState = stateHolder.viewModel.getIsScanningState().collectAsState(),
            toggleScanningOnOff = stateHolder::toggleScanningOnOff
        )

        LazyColumn(content = {
            items(trekDevice.size) {
                DeviceItem(
                    deviceName = trekDevice[it].name.takeIf { name -> !name.isNullOrEmpty() }
                        ?: "N/A",
                    position = it,
                    onItemClick = { index ->
                        stateHolder.setSelectedItemPosition(index)
                        stateHolder.authenticate(
                            stateHolder::connect,
                            stateHolder::launchPasscodeAuthentication
                        )
                    }
                )
            }
        })
    }

    DeviceSelectionDialog(
        state = stateHolder.viewModel.dialogState.collectAsState(),
        onPermissionPositiveEvent = stateHolder::navigateToAppPermissionSettings,
        onLocationDisabledPositiveEvent = stateHolder::navigateToLocationServiceSettings,
        onDismissDialog = stateHolder.viewModel::dismissDialog
    )
}

@Composable
private fun rememberDeviceSelectionScreenStateHolder(
    navController: NavHostController? = LocalNavController.current,
    context: Context = LocalContext.current,
    viewModel: DeviceSelectionViewModel = hiltViewModel(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    multiplePermissionsState: MultiplePermissionsState = rememberMultiplePermissionsState(
        permissions = viewModel.getRequiredPermissions(),
        onPermissionsResult = { viewModel.apply { startScan(getPermissionResult(it)) } }
    )
): DeviceSelectionScreenStateHolder {
    val stateHolder = remember {
        DeviceSelectionScreenStateHolder(
            context, viewModel, navController, coroutineScope, multiplePermissionsState
        )
    }

    val activityResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result -> if (Activity.RESULT_OK == result.resultCode) stateHolder.connect() }
    )

    return stateHolder.apply { setActivityResultLauncher(activityResultLauncher) }
}