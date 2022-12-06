package sg.com.trekstorageauthentication.presentation.screen.unregister_reset_status

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import sg.com.trekstorageauthentication.R
import sg.com.trekstorageauthentication.presentation.component.LocalNavController
import sg.com.trekstorageauthentication.presentation.navigation.Screen

@Composable
fun DisableAuthenticationSuccessScreen() {
    val navController = LocalNavController.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
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
            stringResource(R.string.disable_authentication_successful),
            style = MaterialTheme.typography.h2,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = buildAnnotatedString {
                val fontFamily = FontFamily(Font(R.font.knock_knock))
                val stringArr = stringArrayResource(R.array.disable_authentication_successful_desc)

                append(stringArr[0])

                withStyle(
                    style = SpanStyle(
                        color = Color.Black,
                        fontFamily = fontFamily,
                        fontStyle = FontStyle.Italic
                    )
                ) { append("${stringArr[1]} ") }

                withStyle(
                    style = SpanStyle(
                        color = Color.Red,
                        fontFamily = fontFamily,
                        fontStyle = FontStyle.Italic
                    )
                ) { append("${stringArr[2]}\n") }

                append(stringArr[3])
            },
            style = MaterialTheme.typography.h3,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = {
                navController?.navigate(Screen.DeviceSelectionScreen.route) {
                    popUpTo(0) { inclusive = true }
                }
            },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(48.dp)
        ) {
            Text(stringResource(R.string.go_to_main_screen))
        }
    }
}