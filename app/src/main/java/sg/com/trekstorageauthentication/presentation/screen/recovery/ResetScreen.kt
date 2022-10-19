package sg.com.trekstorageauthentication.presentation.screen.recovery

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import sg.com.trekstorageauthentication.R
import sg.com.trekstorageauthentication.presentation.component.textfield.PasswordTextField
import sg.com.trekstorageauthentication.presentation.component.textfield.PasswordTextFieldState
import sg.com.trekstorageauthentication.presentation.screen.recovery.state.ResetScreenStateHolder

@Composable
fun ResetScreen() {
    val stateHolder = rememberResetScreenStateHolder()

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

        PasswordTextField(
            state = stateHolder.passwordState,
            onValueChange = stateHolder::setPasswordStateValue,
            label = stringResource(R.string.password),
            modifier = Modifier.fillMaxWidth(),
            imeAction = ImeAction.Next,
            keyboardActions = KeyboardActions { stateHolder.clearFocus() }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = stateHolder::showConfirmResetDialog,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) { Text(stringResource(R.string.save)) }
    }
}

@Composable
fun rememberResetScreenStateHolder(
    focusManager: FocusManager = LocalFocusManager.current,
    passwordState: MutableState<PasswordTextFieldState> = mutableStateOf(PasswordTextFieldState()),
): ResetScreenStateHolder {
    return ResetScreenStateHolder(focusManager, passwordState)
}