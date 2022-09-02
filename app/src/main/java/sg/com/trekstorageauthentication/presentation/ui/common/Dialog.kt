package sg.com.trekstorageauthentication.presentation.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties

@Composable
fun Dialog(
    title: String = "",
    content: String = "",
    isCancellable: Boolean = true,
    positiveButtonText: Int = android.R.string.ok,
    negativeButtonText: Int = android.R.string.cancel,
    onNegativeClickEvent: (() -> Unit)? = null,
    onPositiveClickEvent: (() -> Unit)? = null,
    onDismissRequest: (() -> Unit)? = null
) {
    AlertDialog(
        onDismissRequest = { onDismissRequest?.invoke() },
        properties = DialogProperties(
            dismissOnBackPress = isCancellable,
            dismissOnClickOutside = isCancellable
        ),
        title = {
            Text(title)
        },
        text = {
            Text(content)
        },
        buttons = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 8.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                if (onNegativeClickEvent != null) {
                    TextButton(onClick = onNegativeClickEvent) {
                        Text(stringResource(negativeButtonText))
                    }

                    if (onPositiveClickEvent != null) {
                        Spacer(modifier = Modifier.width(16.dp))
                    }
                }

                if (onPositiveClickEvent != null) {
                    TextButton(onClick = onPositiveClickEvent) {
                        Text(stringResource(positiveButtonText))
                    }
                }
            }
        })
}