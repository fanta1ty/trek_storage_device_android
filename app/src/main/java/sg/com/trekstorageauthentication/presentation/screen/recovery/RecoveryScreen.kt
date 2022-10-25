package sg.com.trekstorageauthentication.presentation.screen.recovery

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import sg.com.trekstorageauthentication.R
import sg.com.trekstorageauthentication.presentation.component.LocalNavController
import sg.com.trekstorageauthentication.presentation.component.textfield.PasswordTextField
import sg.com.trekstorageauthentication.presentation.component.textfield.PasswordTextFieldState
import sg.com.trekstorageauthentication.presentation.screen.recovery.state.RecoveryScreenStateHolder

@Composable
fun RecoveryScreen() {
    val stateHolder = rememberRecoveryScreenStateHolder(LocalNavController.current)

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(color = MaterialTheme.colors.primary)
        )

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
                .height(LocalConfiguration.current.screenHeightDp.dp - 50.dp)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                stringResource(R.string.enter_recovery_pin),
                style = MaterialTheme.typography.h2
            )

            Spacer(modifier = Modifier.height(32.dp))

            PasswordTextField(
                state = stateHolder.pinState,
                onValueChange = stateHolder::setPinStateValue,
                label = stringResource(R.string.recovery_pin),
                modifier = Modifier.fillMaxWidth(),
                imeAction = ImeAction.Next,
                keyboardActions = KeyboardActions { stateHolder.clearFocus() }
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = stateHolder::login,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) { Text(stringResource(R.string.confirm)) }
        }
    }
}

@Composable
private fun rememberRecoveryScreenStateHolder(
    navController: NavHostController?,
    context: Context = LocalContext.current,
    viewModel: RecoveryViewModel = hiltViewModel(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    focusManager: FocusManager = LocalFocusManager.current,
    pinState: MutableState<PasswordTextFieldState> = mutableStateOf(PasswordTextFieldState()),
): RecoveryScreenStateHolder {
    return RecoveryScreenStateHolder(
        navController, context, viewModel, coroutineScope, focusManager, pinState
    )
}