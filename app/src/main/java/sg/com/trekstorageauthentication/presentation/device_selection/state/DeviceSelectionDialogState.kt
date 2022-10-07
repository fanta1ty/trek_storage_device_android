package sg.com.trekstorageauthentication.presentation.device_selection.state

class DeviceSelectionDialogState(
    val isShowPermissionsRequestDialog: Boolean = false,
    val isShowBluetoothDisabledDialog: Boolean = false,
    val isLocationServiceDisabledDialog: Boolean = false,
)