package sg.com.trekstorageauthentication.presentation.screen.auth_success.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import sg.com.trekstorageauthentication.R

@Composable
fun AuthSuccessToolbar(
    onUnregisterThumbDrive: () -> Unit,
    onResetThumbDrive: () -> Unit,
) {
    var isExpandDropDownMenu by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(color = MaterialTheme.colors.primary)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Box {
            IconButton(onClick = { isExpandDropDownMenu = true }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "",
                    tint = MaterialTheme.colors.onPrimary,
                )
            }

            DropdownMenu(
                expanded = isExpandDropDownMenu,
                onDismissRequest = { isExpandDropDownMenu = false }) {
                DropdownMenuItem(
                    onClick = {
                        isExpandDropDownMenu = false
                        onUnregisterThumbDrive()
                    }
                ) { Text(stringResource(R.string.disable_authentication)) }

                DropdownMenuItem(
                    onClick = {
                        isExpandDropDownMenu = false
                        onResetThumbDrive()
                    }
                ) { Text(stringResource(R.string.factory_reset)) }
            }
        }
    }
}