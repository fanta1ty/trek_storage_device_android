package sg.com.trekstorageauthentication.presentation.main.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.res.stringResource
import sg.com.trekstorageauthentication.R
import sg.com.trekstorageauthentication.presentation.main.state.MainState
import sg.com.trekstorageauthentication.presentation.ui.common.Dialog

@Composable
fun PermissionDeniedDialog(mainState: State<MainState>, onPositiveEvent: () -> Unit) {
    if (!mainState.value.isPermissionsGranted) {
        Dialog(
            title = stringResource(R.string.permission_required),
            content = stringResource(R.string.permission_permanent_denied_msg),
            isCancellable = false,
            onPositiveClickEvent = onPositiveEvent
        )
    }
}