package sg.com.trekstorageauthentication.presentation.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Snackbar
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun Snackbar(
    contentLabel: String,
    contentColor: Color = Color.White,
    backgroundColor: Color = Color.Black,
    icon: @Composable () -> Unit = {},
    actionColor: Color = Color.Yellow,
    actionLabel: String = "",
    actionEvent: (() -> Unit)? = null
) {
    Snackbar(backgroundColor = backgroundColor) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon()

            Text(contentLabel, color = contentColor)

            if (actionEvent != null) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    TextButton(onClick = actionEvent) {
                        Text(actionLabel, color = actionColor)
                    }
                }
            }
        }
    }
}