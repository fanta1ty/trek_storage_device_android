package sg.com.trekstorageauthentication.presentation.device_selection.state

data class DeviceSelectionScreenState(
    val dialogState: DeviceSelectionDialogState = DeviceSelectionDialogState(),
    val isScanning: Boolean = false,
    val isShowDeviceList: Boolean = false
)