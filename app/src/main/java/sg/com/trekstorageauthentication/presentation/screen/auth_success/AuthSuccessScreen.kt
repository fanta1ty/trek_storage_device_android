package sg.com.trekstorageauthentication.presentation.screen.auth_success

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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

@Composable
fun AuthSuccessScreen() {
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
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_auth_success),
                    contentDescription = null,
                    modifier = Modifier
                        .width(150.dp)
                        .height(150.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    stringResource(R.string.authentication_successful),
                    style = MaterialTheme.typography.h4,
                )

                Spacer(modifier = Modifier.height(48.dp))

                Text(
                    stringResource(R.string.authentication_successful_desc),
                    style = MaterialTheme.typography.h4.copy(textAlign = TextAlign.Center)
                )
            }
        }
    }
}