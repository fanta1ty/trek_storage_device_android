package sg.com.trekstorageauthentication.presentation.screen.auth_success.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import androidx.compose.ui.window.Dialog
import sg.com.trekstorageauthentication.R
import sg.com.trekstorageauthentication.presentation.component.Dialog
import sg.com.trekstorageauthentication.presentation.screen.auth_success.state.AuthSuccessDialogState

@Composable
fun ConfirmDisableAuthenticationDialog(
    state: State<AuthSuccessDialogState>,
    onPositiveClick: () -> Unit,
    onNegativeClick: () -> Unit
) {
    if (state.value.isShowing) {
        Dialog(
            title = stringResource(R.string.disable_authentication_confirm_title),
            content = { ConfirmDisableAuthenticationDialogContent() },
            onPositiveClickEvent = onPositiveClick,
            onNegativeClickEvent = onNegativeClick
        )
    }
}

@Composable
fun ConfirmFactoryResetDialog(
    state: State<AuthSuccessDialogState>,
    onPositiveClick: () -> Unit,
    onNegativeClick: () -> Unit
) {
    if (state.value.isShowing) {
        Dialog(
            title = stringResource(R.string.factory_reset_confirm_title),
            content = { ConfirmFactoryResetDialogContent() },
            onPositiveClickEvent = onPositiveClick,
            onNegativeClickEvent = onNegativeClick
        )
    }
}

@Composable
fun FactoryResetProgressDialog(
    isShowing: Boolean,
) {
    if (isShowing) {
        Dialog(onDismissRequest = {}) {
            Card(
                backgroundColor = MaterialTheme.colors.surface,
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 48.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = stringResource(id = R.string.please_wait),
                        style = MaterialTheme.typography.h2,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    CircularProgressIndicator(modifier = Modifier.size(100.dp))
                }
            }
        }
    }
}

@Composable
fun DisableAuthenticationProgressDialog(
    isShowing: Boolean,
) {
    if (isShowing) {
        Dialog(onDismissRequest = {}) {
            Card(
                backgroundColor = MaterialTheme.colors.surface,
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 48.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = stringResource(id = R.string.please_wait),
                        style = MaterialTheme.typography.h2,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    CircularProgressIndicator(modifier = Modifier.size(100.dp))
                }
            }
        }
    }
}

@Composable
private fun ConfirmDisableAuthenticationDialogContent() {
    val fontFamily = FontFamily(Font(R.font.knock_knock))
    val stringArr = stringArrayResource(R.array.disable_authentication_confirm_desc)

    Text(
        text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    color = Color.Black,
                    fontFamily = fontFamily,
                    fontStyle = FontStyle.Italic
                )
            ) { append("${stringArr[0]} ") }

            withStyle(
                style = SpanStyle(
                    color = Color.Red,
                    fontFamily = fontFamily,
                    fontStyle = FontStyle.Italic
                )
            ) { append("${stringArr[1]}  ") }

            append(stringArr[2])
        },
        color = Color.Black
    )
}

@Composable
private fun ConfirmFactoryResetDialogContent() {
    val fontFamily = FontFamily(Font(R.font.knock_knock))
    val stringArr = stringArrayResource(R.array.factory_reset_confirm_desc)

    Text(
        text = buildAnnotatedString {
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
            ) { append("${stringArr[2]}  ") }

            append(stringArr[3])

            withStyle(style = SpanStyle(color = Color.Red)) { append(stringArr[4]) }

            withStyle(
                style = SpanStyle(
                    color = Color.Black,
                    fontFamily = fontFamily,
                    fontStyle = FontStyle.Italic
                )
            ) { append("${stringArr[5]} ") }

            withStyle(
                style = SpanStyle(
                    color = Color.Red,
                    fontFamily = fontFamily,
                    fontStyle = FontStyle.Italic
                )
            ) { append("${stringArr[6]}  ") }

            withStyle(style = SpanStyle(color = Color.Red)) { append(stringArr[7]) }
        },
        color = Color.Black
    )
}