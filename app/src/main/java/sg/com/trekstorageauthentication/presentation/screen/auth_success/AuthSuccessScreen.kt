package sg.com.trekstorageauthentication.presentation.screen.auth_success

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import sg.com.trekstorageauthentication.R
import sg.com.trekstorageauthentication.presentation.component.LocalNavController
import sg.com.trekstorageauthentication.presentation.screen.auth_success.component.*
import sg.com.trekstorageauthentication.presentation.screen.auth_success.state.AuthSuccessScreenStateHolder

@Composable
fun AuthSuccessScreen(isRegister: Boolean) {
    val stateHolder = rememberAuthSuccessScreenStateHolder(isRegister)

    Column(modifier = Modifier.fillMaxSize()) {
        AuthSuccessToolbar(
            stateHolder::navigateChangePinScreen,
            stateHolder.viewModel::showConfirmDisableAuthenticationDialog,
            stateHolder.viewModel::showConfirmFactoryResetThumbDriveDialog,
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.ic_auth_success),
                contentDescription = null,
                modifier = Modifier
                    .width(150.dp)
                    .height(150.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = if (stateHolder.isRegister)
                    stringResource(R.string.registration_successful) else
                    stringResource(R.string.authentication_successful),
                style = MaterialTheme.typography.h2,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                if (stateHolder.isRegister)
                    stringResource(R.string.registration_successful_desc) else
                    stringResource(R.string.authentication_successful_desc),
                style = MaterialTheme.typography.h3,
                textAlign = TextAlign.Center,
            )
        }

    }

    ConfirmDisableAuthenticationDialog(
        stateHolder.viewModel.unregisterDialogState.collectAsState(),
        stateHolder.viewModel::disableAuthentication,
        stateHolder.viewModel::dismissDisableAuthenticationDialog,
    )

    ConfirmFactoryResetDialog(
        stateHolder.viewModel.resetDialogState.collectAsState(),
        stateHolder.viewModel::factoryResetThumbDrive,
        stateHolder.viewModel::dismissFactoryResetDialog,
    )

    DisableAuthenticationProgressDialog(isShowing = stateHolder.viewModel.thumbDriveDisablingAuthentication)
    FactoryResetProgressDialog(isShowing = stateHolder.viewModel.thumbDriveFactoryResetting)
}

@Composable
private fun rememberAuthSuccessScreenStateHolder(
    isRegister: Boolean,
    navController: NavHostController? = LocalNavController.current,
    context: Context = LocalContext.current,
    viewModel: AuthSuccessViewModel = hiltViewModel(),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
): AuthSuccessScreenStateHolder {
    return remember {
        AuthSuccessScreenStateHolder(isRegister, navController, context, viewModel, coroutineScope)
    }
}