package sg.com.trekstorageauthentication.presentation.screen.unregister_reset_status

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import sg.com.trekstorageauthentication.R
import sg.com.trekstorageauthentication.presentation.component.LocalNavController

@Composable
fun DisableAuthenticationFailedScreen() {
    val navController = LocalNavController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
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
            stringResource(R.string.disable_authentication_failed),
            style = MaterialTheme.typography.h2,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            stringResource(R.string.please_try_again),
            style = MaterialTheme.typography.h3,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = {
                navController?.popBackStack()
            },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(48.dp)
        ) {
            Text(stringResource(R.string.go_back))
        }
    }
}