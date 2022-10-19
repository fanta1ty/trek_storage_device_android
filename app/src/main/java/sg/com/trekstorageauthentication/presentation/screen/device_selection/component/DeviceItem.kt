package sg.com.trekstorageauthentication.presentation.screen.device_selection.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DeviceItem(
    deviceName: String,
    position: Int,
    onItemClick: (i: Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 32.dp)
            .clickable(onClick = { onItemClick(position) })
    ) {
        Text(deviceName)

        Spacer(modifier = Modifier.height(16.dp))

        Divider(color = Color.LightGray)
    }
}