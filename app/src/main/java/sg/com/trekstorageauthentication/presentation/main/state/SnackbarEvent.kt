package sg.com.trekstorageauthentication.presentation.main.state

import androidx.compose.material.SnackbarDuration
import java.util.*

data class SnackbarEvent(
    val msg: String,
    val duration: SnackbarDuration = SnackbarDuration.Short,
    val id: Int = Date().hashCode()
)