package sg.com.trekstorageauthentication.presentation.state

data class MainState(
    val isPermissionsGranted: Boolean = true,
    val isBluetoothEnabled: Boolean = true,
    val isLocationServiceEnabled: Boolean = true,
    val isLoading: Boolean = false,
)