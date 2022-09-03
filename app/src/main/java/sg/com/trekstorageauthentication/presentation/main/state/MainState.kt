package sg.com.trekstorageauthentication.presentation.main.state

data class MainState(
    val isPermissionsGranted: Boolean = true,
    val isBluetoothEnabled: Boolean = true,
    val isLocationServiceEnabled: Boolean = true,
)