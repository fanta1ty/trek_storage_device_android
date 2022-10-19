package sg.com.trekstorageauthentication.presentation.screen.device_selection.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import sg.com.trekstorageauthentication.R

@Composable
fun DeviceSelectionToolbar(
    isScanningState: State<Boolean>,
    toggleScanningOnOff: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(color = MaterialTheme.colors.primary)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            stringResource(R.string.scanning_devices),
            style = MaterialTheme.typography.h4,
            color = MaterialTheme.colors.onPrimary
        )

        Row(
            modifier = Modifier.fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isScanningState.value) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .width(24.dp)
                        .height(24.dp),
                    color = MaterialTheme.colors.onPrimary,
                    strokeWidth = 2.dp
                )

                Spacer(modifier = Modifier.width(16.dp))
            }

            TextButton(onClick = toggleScanningOnOff) {
                val stringId = if (isScanningState.value) R.string.stop else R.string.scan
                Text(
                    stringResource(stringId),
                    style = MaterialTheme.typography.h4,
                    color = MaterialTheme.colors.onPrimary
                )
            }
        }
    }
}