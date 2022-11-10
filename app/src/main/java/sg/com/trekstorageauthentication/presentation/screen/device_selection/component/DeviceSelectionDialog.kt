package sg.com.trekstorageauthentication.presentation.screen.device_selection.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.res.stringResource
import sg.com.trekstorageauthentication.R
import sg.com.trekstorageauthentication.presentation.component.LoadingDialog
import sg.com.trekstorageauthentication.presentation.screen.device_selection.state.DeviceSelectionDialogState
import sg.com.trekstorageauthentication.presentation.component.Dialog

@Composable
fun DeviceSelectionDialog(
    state: State<DeviceSelectionDialogState>,
    onPermissionPositiveEvent: () -> Unit,
    onLocationDisabledPositiveEvent: () -> Unit,
    onBluetoothDisabledPositiveEvent: () -> Unit
) {
    val value = state.value

    if (value.isShowPermissionsRequestDialog) {
        Dialog(
            title = stringResource(R.string.permission_required),
            content = stringResource(R.string.permission_permanent_denied_msg),
            isCancellable = false,
            onPositiveClickEvent = onPermissionPositiveEvent
        )
    }

    if (value.isShowLocationServiceDisabledDialog) {
        Dialog(
            title = stringResource(R.string.location_required),
            content = stringResource(R.string.location_disabled_msg),
            isCancellable = false,
            onPositiveClickEvent = onLocationDisabledPositiveEvent
        )
    }

    if (value.isShowBluetoothDisabledDialog) {
        Dialog(
            title = stringResource(R.string.bluetooth_required),
            content = stringResource(R.string.bluetooth_disabled_msg),
            isCancellable = false,
            onPositiveClickEvent = onBluetoothDisabledPositiveEvent
        )
    }

    if (value.isShowLoadingDialog) {
        LoadingDialog(
            title = stringResource(R.string.please_wait),
            content = stringResource(R.string.connecting_to_pc),
            isCancellable = false
        )
    }
}