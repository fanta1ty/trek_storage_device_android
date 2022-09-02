package sg.com.trekstorageauthentication.presentation.main.state

import sg.com.trekstorageauthentication.service.ble.BleConnectionState

data class MainState(
    var isPermissionsGranted: Boolean = true,
    var isBluetoothEnabled: Boolean = true,
    var isLocationServiceEnabled: Boolean = true,
    var bleConnectionState: BleConnectionState = BleConnectionState.CONNECTED
)