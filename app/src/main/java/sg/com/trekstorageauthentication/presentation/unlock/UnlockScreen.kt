package sg.com.trekstorageauthentication.presentation.unlock

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import sg.com.trekstorageauthentication.R
import sg.com.trekstorageauthentication.presentation.MainViewModel
import sg.com.trekstorageauthentication.presentation.ui.common.noRippleClickable
import sg.com.trekstorageauthentication.presentation.ui.common.textfield.PasswordTextField
import sg.com.trekstorageauthentication.presentation.ui.common.textfield.PasswordTextFieldState
import sg.com.trekstorageauthentication.presentation.unlock.state.UnlockScreenStateHolder

@Composable
fun UnlockScreen() {
    val stateHolder = rememberLoginScreenStateHolder()

    LaunchedEffect(key1 = true) { launch { stateHolder.registerBiometricAuthEvent() } }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
            .height(LocalConfiguration.current.screenHeightDp.dp)
            .padding(16.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.logo_login),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        )

        PasswordTextField(
            state = stateHolder.passwordState,
            onValueChange = stateHolder::setPasswordState,
            label = stringResource(R.string.enter_password),
            modifier = Modifier.fillMaxWidth(),
            keyboardActions = KeyboardActions { stateHolder.clearFocus() }
        )

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                stringResource(R.string.reset_your_password),
                color = MaterialTheme.colors.primary,
                modifier = Modifier.noRippleClickable(
                    onClick = stateHolder::resetSettings
                )
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = stateHolder::authenticate,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) { Text(stringResource(R.string.log_in)) }
    }
}

@Composable
private fun rememberLoginScreenStateHolder(
    context: Context = LocalContext.current,
    focusManager: FocusManager = LocalFocusManager.current,
    viewModel: MainViewModel = hiltViewModel(context as FragmentActivity),
    passwordState: MutableState<PasswordTextFieldState> = mutableStateOf(PasswordTextFieldState())
): UnlockScreenStateHolder {
    return remember { UnlockScreenStateHolder(context, focusManager, viewModel, passwordState) }
}