package sg.com.trekstorageauthentication.presentation.device_selection.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DeviceItem(deviceName: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 8.dp)
    ) {
        Text(deviceName)

        Spacer(modifier = Modifier.height(16.dp))

        Divider(color = Color.DarkGray)
    }
}