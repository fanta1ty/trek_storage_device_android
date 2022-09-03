package sg.com.trekstorageauthentication.presentation.login.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import sg.com.trekstorageauthentication.R
import sg.com.trekstorageauthentication.presentation.login.state.LoginScreenStateHolder
import sg.com.trekstorageauthentication.presentation.ui.common.noRippleClickable
import sg.com.trekstorageauthentication.presentation.ui.common.textfield.PasswordTextField
import sg.com.trekstorageauthentication.presentation.ui.navigation.Screen

@Composable
fun LoginMainContent(
    stateHolder: LoginScreenStateHolder,
    navController: NavHostController
) {
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
                onClick = stateHolder::passwordAuthenticate,
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
                        .noRippleClickable(onClick = stateHolder::biometricAuthenticate)
                )
            }
        }
    }
}