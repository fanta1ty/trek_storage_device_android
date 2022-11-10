package sg.com.trekstorageauthentication.presentation.screen.auth_success.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import sg.com.trekstorageauthentication.R
import sg.com.trekstorageauthentication.presentation.screen.auth_success.state.AuthSuccessDialogState
import sg.com.trekstorageauthentication.presentation.component.Dialog
import androidx.compose.ui.window.Dialog

@Composable
fun ConfirmDisableAuthenticationDialog(
    state: State<AuthSuccessDialogState>,
    onPositiveClick: () -> Unit,
    onNegativeClick: () -> Unit
) {
    if (state.value.isShowing) {
        Dialog(
            title = stringResource(R.string.disable_authentication_confirm_title),
            content = stringResource(R.string.disable_authentication_confirm_desc),
            onPositiveClickEvent = onPositiveClick,
            onNegativeClickEvent = onNegativeClick,
        )
    }
}

@Composable
fun ConfirmFactoryResetDialog(
    state: State<AuthSuccessDialogState>,
    onPositiveClick: () -> Unit,
    onNegativeClick: () -> Unit
) {
    if (state.value.isShowing) {
        Dialog(
            title = stringResource(R.string.factory_reset_confirm_title),
            content = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        stringResource(R.string.factory_reset_confirm_desc),
                    )
                    Text(
                        stringResource(R.string.factory_reset_confirm_desc_2),
                        color = Color.Red,
                    )
                }
            },
            onPositiveClickEvent = onPositiveClick,
            onNegativeClickEvent = onNegativeClick,
        )
    }
}

@Composable
fun FactoryResetProgressDialog(
    isShowing: Boolean,
) {
    if (isShowing) {
        Dialog(onDismissRequest = {}) {
            Card(
                backgroundColor = MaterialTheme.colors.surface,
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 48.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = stringResource(id = R.string.factory_resetting),
                        style = MaterialTheme.typography.h2,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    CircularProgressIndicator(modifier = Modifier.size(100.dp))
                }
            }
        }
    }
}

@Composable
fun DisableAuthenticationProgressDialog(
    isShowing: Boolean,
) {
    if (isShowing) {
        Dialog(onDismissRequest = {}) {
            Card(
                backgroundColor = MaterialTheme.colors.surface,
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 48.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = stringResource(id = R.string.disabling_authentication),
                        style = MaterialTheme.typography.h2,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    CircularProgressIndicator(modifier = Modifier.size(100.dp))
                }
            }
        }
    }
}