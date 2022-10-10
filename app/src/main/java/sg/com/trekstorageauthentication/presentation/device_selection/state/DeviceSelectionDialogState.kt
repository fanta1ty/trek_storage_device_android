package sg.com.trekstorageauthentication.presentation.device_selection.state

data class DeviceSelectionDialogState(
    val isShowPermissionsRequestDialog: Boolean = false,
    val isShowBluetoothDisabledDialog: Boolean = false,
    val isShowLocationServiceDisabledDialog: Boolean = false,
)