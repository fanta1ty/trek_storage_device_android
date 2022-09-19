package sg.com.trekstorageauthentication.service.ble

enum class BleResponseType {
    REGISTER_PASSWORD_SUCCESS,
    REGISTER_PASSWORD_FAIL,
    RESET_SETTINGS_SUCCESS,
    RESET_SETTINGS_FAIL,
    LOG_IN_SUCCESS,
    LOG_IN_FAIL,
    LOG_OUT_SUCCESS,
    LOG_OUT_FAIL,
    PASSWORD_STATUS
}