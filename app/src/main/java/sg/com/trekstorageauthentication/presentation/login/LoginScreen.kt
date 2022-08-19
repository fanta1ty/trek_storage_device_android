package sg.com.trekstorageauthentication.presentation.login

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import sg.com.trekstorageauthentication.R
import sg.com.trekstorageauthentication.presentation.login.component.AutoSizeText
import sg.com.trekstorageauthentication.presentation.login.component.BiometricAuthenticationDisabledDialog
import sg.com.trekstorageauthentication.presentation.login.state.LoginScreenStateHolder
import sg.com.trekstorageauthentication.presentation.ui.common.noRippleClickable
import sg.com.trekstorageauthentication.presentation.ui.navigation.Screen

@Composable
fun LoginScreen(navController: NavHostController) {
    val stateHolder = rememberLoginScreenStateHolder()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.logo_login),
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        )

        OutlinedTextField(
            value = stateHolder.password.value,
            onValueChange = { newText -> stateHolder.setPassword(newText) },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1,
            label = { Text(stringResource(R.string.enter_password)) }
        )

        Spacer(modifier = Modifier.height(16.dp))

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
                    .weight(7F),
            ) { Text(stringResource(R.string.unlock)) }

            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(3F),
                contentAlignment = Alignment.Center
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

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            AutoSizeText(
                modifier = Modifier.noRippleClickable {
                    navController.navigate(Screen.RegisterScreen.route)
                },
                text = buildAnnotatedString {
                    append(stringResource(R.string.have_not_set_up_password_yet))
                    append(' ')

                    withStyle(style = SpanStyle(color = MaterialTheme.colors.primary)) {
                        append(stringResource(R.string.set_up_now))
                    }
                    append('.')
                })
        }
    }

    BiometricAuthenticationDisabledDialog(
        state = stateHolder.viewModel.biometricAuthenticationReadyState,
        onNegativeEvent = stateHolder::dismissBiometricAuthenticationDisabledDialog
    )
}

@Composable
fun rememberLoginScreenStateHolder(
    context: Context = LocalContext.current,
    viewModel: LoginViewModel = hiltViewModel(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    password: MutableState<String> = mutableStateOf("")
): LoginScreenStateHolder {
    return remember {
        LoginScreenStateHolder(context, viewModel, coroutineScope, password)
    }
}