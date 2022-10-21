package sg.com.trekstorageauthentication.service.ble

enum class BleResponseType {
    REGISTER_PIN_SUCCESS,
    REGISTER_PIN_FAIL,
    RESET_SETTINGS_SUCCESS,
    RESET_SETTINGS_FAIL,
    LOG_IN_SUCCESS,
    LOG_IN_FAIL,
    PIN_STATUS
}