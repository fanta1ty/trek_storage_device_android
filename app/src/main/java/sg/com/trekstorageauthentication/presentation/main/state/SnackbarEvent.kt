package sg.com.trekstorageauthentication.presentation.main.state

import androidx.compose.material.SnackbarDuration

data class SnackbarEvent(
    val msg: String,
    val duration: SnackbarDuration = SnackbarDuration.Short
)