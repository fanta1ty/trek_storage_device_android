package sg.com.trekstorageauthentication.presentation.register_password

import android.content.Context
import androidx.compose.foundation.Image
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import sg.com.trekstorageauthentication.R
import sg.com.trekstorageauthentication.presentation.MainViewModel
import sg.com.trekstorageauthentication.presentation.register_password.state.RegisterPasswordScreenStateHolder
import sg.com.trekstorageauthentication.presentation.ui.common.textfield.PasswordTextField
import sg.com.trekstorageauthentication.presentation.ui.common.textfield.PasswordTextFieldState

@Composable
fun RegisterPasswordScreen() {
    val stateHolder = rememberRegisterPasswordScreenStateHolder()

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.logo_register),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            stringResource(R.string.register_password),
            style = MaterialTheme.typography.h2
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PasswordTextField(
                state = stateHolder.passwordState,
                onValueChange = stateHolder::setPasswordStateValue,
                label = stringResource(R.string.password),
                modifier = Modifier.fillMaxWidth(),
                imeAction = ImeAction.Next,
                keyboardActions = KeyboardActions { stateHolder.moveFocusDown() }
            )

            Spacer(modifier = Modifier.height(16.dp))

            PasswordTextField(
                state = stateHolder.confirmPasswordState,
                onValueChange = stateHolder::setConfirmPasswordStateValue,
                label = stringResource(R.string.confirm_password),
                modifier = Modifier.fillMaxWidth(),
                keyboardActions = KeyboardActions { stateHolder.clearFocus() }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = stateHolder::save,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) { Text(stringResource(R.string.save)) }
        }
    }
}

@Composable
fun rememberRegisterPasswordScreenStateHolder(
    context: Context = LocalContext.current,
    focusManager: FocusManager = LocalFocusManager.current,
    viewModel: MainViewModel = hiltViewModel(),
    passwordState: MutableState<PasswordTextFieldState> = mutableStateOf(PasswordTextFieldState()),
    confirmPasswordState: MutableState<PasswordTextFieldState> = mutableStateOf(
        PasswordTextFieldState()
    )
): RegisterPasswordScreenStateHolder {
    return remember {
        RegisterPasswordScreenStateHolder(
            context,
            focusManager,
            viewModel,
            passwordState,
            confirmPasswordState
        )
    }
}