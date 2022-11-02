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
import sg.com.trekstorageauthentication.presentation.screen.auth_success.component.AuthSuccessToolbar
import sg.com.trekstorageauthentication.presentation.screen.auth_success.component.ConfirmResetThumbDriveDialog
import sg.com.trekstorageauthentication.presentation.screen.auth_success.state.AuthSuccessScreenStateHolder

@Composable
fun AuthSuccessScreen() {
    val stateHolder = rememberAuthSuccessScreenStateHolder()

    Column(modifier = Modifier.fillMaxSize()) {
        AuthSuccessToolbar(
            stateHolder::navigateChangePinScreen,
            stateHolder.viewModel::showConfirmResetThumbDriveDialog
        )

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
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
                    stringResource(R.string.authentication_successful),
                    style = MaterialTheme.typography.h4,
                )

                Spacer(modifier = Modifier.height(48.dp))

                Text(
                    stringResource(R.string.authentication_successful_desc),
                    style = MaterialTheme.typography.h4.copy(textAlign = TextAlign.Center)
                )
            }
        }
    }

    ConfirmResetThumbDriveDialog(
        stateHolder.viewModel.dialogState.collectAsState(),
        stateHolder.viewModel::resetThumbDrive,
        stateHolder.viewModel::dismissDialog,
    )
}

@Composable
private fun rememberAuthSuccessScreenStateHolder(
    navController: NavHostController? = LocalNavController.current,
    context: Context = LocalContext.current,
    viewModel: AuthSuccessViewModel = hiltViewModel(),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
): AuthSuccessScreenStateHolder {
    return remember {
        AuthSuccessScreenStateHolder(navController, context, viewModel, coroutineScope)
    }
}