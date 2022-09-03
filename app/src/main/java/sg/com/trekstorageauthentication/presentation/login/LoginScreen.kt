package sg.com.trekstorageauthentication.presentation.login

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import sg.com.trekstorageauthentication.presentation.login.component.BiometricAuthenticationDisabledDialog
import sg.com.trekstorageauthentication.presentation.login.component.LoginMainContent
import sg.com.trekstorageauthentication.presentation.login.state.LoginScreenStateHolder
import sg.com.trekstorageauthentication.presentation.ui.common.textfield.PasswordTextFieldState

@Composable
fun LoginScreen(navController: NavHostController) {
    val stateHolder = rememberLoginScreenStateHolder()

    LoginMainContent(
        stateHolder = stateHolder,
        navController = navController
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
    viewModel: LoginViewModel = hiltViewModel(),
    password: MutableState<PasswordTextFieldState> = mutableStateOf(PasswordTextFieldState())
): LoginScreenStateHolder {
    return remember { LoginScreenStateHolder(context, focusManager, viewModel, password) }
}