package sg.com.trekstorageauthentication.presentation.home.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.res.stringResource
import sg.com.trekstorageauthentication.R
import sg.com.trekstorageauthentication.presentation.home.state.HomeState
import sg.com.trekstorageauthentication.presentation.ui.common.Dialog

@Composable
fun HomeDialog(
    state: State<HomeState>,
    logOut: () -> Unit,
    resetSettings: () -> Unit,
    dismissDialog: () -> Unit,
) {
    if (state.value.isShowConfirmLogOutDialog) {
        Dialog(
            title = stringResource(R.string.dialog_log_out_title),
            content = stringResource(R.string.dialog_log_out_content),
            positiveButtonText = R.string.confirm,
            onPositiveClickEvent = logOut,
            onNegativeClickEvent = dismissDialog,
            onDismissRequest = dismissDialog
        )
    }

    if (state.value.isShowConfirmResetSettingsDialog) {
        Dialog(
            title = stringResource(R.string.dialog_reset_settings_title),
            content = stringResource(R.string.dialog_reset_settings_content),
            positiveButtonText = R.string.confirm,
            onPositiveClickEvent = resetSettings,
            onNegativeClickEvent = dismissDialog,
            onDismissRequest = dismissDialog
        )
    }
}