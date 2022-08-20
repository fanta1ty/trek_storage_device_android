package sg.com.trekstorageauthentication.presentation.register

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.navigation.NavHostController
import sg.com.trekstorageauthentication.R
import sg.com.trekstorageauthentication.presentation.register.state.RegisterScreenStateHolder
import sg.com.trekstorageauthentication.presentation.ui.common.textfield.PasswordTextField
import sg.com.trekstorageauthentication.presentation.ui.common.textfield.PasswordTextFieldState

@Composable
fun RegisterScreen(navController: NavHostController) {
    val stateHolder = rememberRegisterScreenStateHolder()

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .padding(10.dp)
    ) {
        IconButton(
            onClick = { navController.navigateUp() },
            modifier = Modifier
                .width(44.dp)
                .height(44.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "",
                modifier = Modifier
                    .width(32.dp)
                    .height(32.dp)
            )
        }

        Image(
            painter = painterResource(R.drawable.logo_register),
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            stringResource(R.string.set_up),
            style = MaterialTheme.typography.h2
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PasswordTextField(
                state = stateHolder.currentPassword,
                onValueChange = stateHolder::setCurrentPassword,
                label = stringResource(R.string.current_password),
                modifier = Modifier.fillMaxWidth(),
                imeAction = ImeAction.Next,
                keyboardActions = KeyboardActions { stateHolder.moveFocusDown() }
            )

            Spacer(modifier = Modifier.height(16.dp))

            PasswordTextField(
                state = stateHolder.newPassword,
                onValueChange = stateHolder::setNewPassword,
                label = stringResource(R.string.new_password),
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
fun rememberRegisterScreenStateHolder(
    context: Context = LocalContext.current,
    focusManager: FocusManager = LocalFocusManager.current,
    currentPassword: MutableState<PasswordTextFieldState> = mutableStateOf(PasswordTextFieldState()),
    newPassword: MutableState<PasswordTextFieldState> = mutableStateOf(PasswordTextFieldState()),
): RegisterScreenStateHolder {
    return remember {
        RegisterScreenStateHolder(context, focusManager, currentPassword, newPassword)
    }
}