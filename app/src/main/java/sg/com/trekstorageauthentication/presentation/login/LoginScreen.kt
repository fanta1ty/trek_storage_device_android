package sg.com.trekstorageauthentication.presentation.login

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import sg.com.trekstorageauthentication.R
import sg.com.trekstorageauthentication.presentation.login.component.BiometricAuthenticationDisabledDialog
import sg.com.trekstorageauthentication.presentation.login.state.LoginScreenStateHolder
import sg.com.trekstorageauthentication.presentation.ui.common.Snackbar
import sg.com.trekstorageauthentication.presentation.ui.common.noRippleClickable
import sg.com.trekstorageauthentication.presentation.ui.common.textfield.PasswordTextField
import sg.com.trekstorageauthentication.presentation.ui.common.textfield.PasswordTextFieldState
import sg.com.trekstorageauthentication.presentation.ui.navigation.Screen

@Composable
fun LoginScreen(navController: NavHostController) {
    val stateHolder = rememberLoginScreenStateHolder()

    Scaffold(
        scaffoldState = stateHolder.scaffoldState,
        snackbarHost = { hostState ->
            SnackbarHost(
                hostState = hostState,
                snackbar = { data -> Snackbar(data.message, backgroundColor = Color.Green) }
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth()
                    .height(LocalConfiguration.current.screenHeightDp.dp)
                    .padding(16.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.logo_login),
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                )

                PasswordTextField(
                    state = stateHolder.password,
                    onValueChange = stateHolder::setPassword,
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
                        stringResource(R.string.reset_password),
                        color = MaterialTheme.colors.primary,
                        modifier = Modifier.noRippleClickable {
                            navController.navigate(Screen.RegisterScreen.route)
                        }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Button(
                        onClick = { stateHolder.passwordAuthenticate() },
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1F),
                    ) { Text(stringResource(R.string.unlock)) }

                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(90.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_face_id),
                            contentDescription = "",
                            tint = Color.Unspecified,
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(48.dp)
                                .noRippleClickable { stateHolder.biometricAuthenticate() }
                        )
                    }
                }
            }
        }
    )

    BiometricAuthenticationDisabledDialog(
        state = stateHolder.viewModel.biometricAuthenticationReadyState,
        onNegativeEvent = stateHolder::dismissBiometricAuthenticationDisabledDialog,
        onDismissRequest = stateHolder::dismissBiometricAuthenticationDisabledDialog
    )
}

@Composable
private fun rememberLoginScreenStateHolder(
    context: Context = LocalContext.current,
    focusManager: FocusManager = LocalFocusManager.current,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    viewModel: LoginViewModel = hiltViewModel(),
    password: MutableState<PasswordTextFieldState> = mutableStateOf(PasswordTextFieldState())
): LoginScreenStateHolder {
    return remember {
        LoginScreenStateHolder(
            context,
            focusManager,
            coroutineScope,
            scaffoldState,
            viewModel,
            password
        )
    }
}