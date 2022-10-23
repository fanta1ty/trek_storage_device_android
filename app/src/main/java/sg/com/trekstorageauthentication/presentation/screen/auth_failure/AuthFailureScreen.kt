package sg.com.trekstorageauthentication.presentation.screen.auth_failure

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import sg.com.trekstorageauthentication.R
import sg.com.trekstorageauthentication.presentation.navigation.Screen

@Composable
fun AuthFailureScreen(navController: NavHostController) {
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(color = MaterialTheme.colors.primary)
        )

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_auth_fail),
                    contentDescription = null,
                    modifier = Modifier
                        .width(150.dp)
                        .height(150.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    stringResource(R.string.authentication_fail),
                    style = MaterialTheme.typography.h4
                )

                Spacer(modifier = Modifier.height(48.dp))

                Button(
                    onClick = {
                        navController.navigate(Screen.RecoveryScreen.route) {
                            popUpTo(Screen.AuthFailureScreen.route) { inclusive = true }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text(stringResource(R.string.enter_recovery_pin))
                }
            }
        }
    }
}