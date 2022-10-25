package sg.com.trekstorageauthentication.presentation.screen.device_selection.state

data class DeviceSelectionDialogState(
    val isShowPermissionsRequestDialog: Boolean = false,
    val isShowBluetoothDisabledDialog: Boolean = false,
    val isShowLocationServiceDisabledDialog: Boolean = false,
    val isShowLoadingDialog: Boolean = false,
)