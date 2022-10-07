package sg.com.trekstorageauthentication.presentation.device_selection.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.res.stringResource
import sg.com.trekstorageauthentication.R
import sg.com.trekstorageauthentication.presentation.main.state.MainState
import sg.com.trekstorageauthentication.presentation.ui.common.Dialog

@Composable
fun DeviceSelectionDialog(
    state: State<MainState>,
    onPermissionPositiveEvent: () -> Unit,
    onLocationDisabledPositiveEvent: () -> Unit,
    onBluetoothDisabledPositiveEvent: () -> Unit
) {
    val value = state.value

    if (!value.isPermissionsGranted) {
        Dialog(
            title = stringResource(R.string.permission_required),
            content = stringResource(R.string.permission_permanent_denied_msg),
            isCancellable = false,
            onPositiveClickEvent = onPermissionPositiveEvent
        )
    }

    if (!value.isLocationServiceEnabled) {
        Dialog(
            title = stringResource(R.string.location_required),
            content = stringResource(R.string.location_disabled_msg),
            isCancellable = false,
            onPositiveClickEvent = onLocationDisabledPositiveEvent
        )
    }

    if (!value.isBluetoothEnabled) {
        Dialog(
            title = stringResource(R.string.bluetooth_required),
            content = stringResource(R.string.bluetooth_disabled_msg),
            isCancellable = false,
            onPositiveClickEvent = onBluetoothDisabledPositiveEvent
        )
    }
}