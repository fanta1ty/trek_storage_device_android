package sg.com.trekstorageauthentication.presentation.screen.auth_success.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.res.stringResource
import sg.com.trekstorageauthentication.R
import sg.com.trekstorageauthentication.presentation.screen.auth_success.state.AuthSuccessDialogState
import sg.com.trekstorageauthentication.presentation.ui.common.Dialog

@Composable
fun ConfirmResetThumbDriveDialog(
    state: State<AuthSuccessDialogState>,
    onPositiveClick: () -> Unit,
    onNegativeClick: () -> Unit
) {
    if (state.value.isShowConfirmResetThumbDriveDialog) {
        Dialog(
            title = stringResource(R.string.reset_thumb_drive_confirm_title),
            content = stringResource(R.string.reset_thumb_drive_confirm_desc),
            onPositiveClickEvent = onPositiveClick,
            onNegativeClickEvent = onNegativeClick,
        )
    }
}