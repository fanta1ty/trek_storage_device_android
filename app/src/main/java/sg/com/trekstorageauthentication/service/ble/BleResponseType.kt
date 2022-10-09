package sg.com.trekstorageauthentication.service.ble

enum class BleResponseType {
    ALREADY_LOG_IN,
    NOT_ALREADY_LOG_IN,
    REGISTER_PASSWORD_SUCCESS,
    REGISTER_PASSWORD_FAIL,
    RESET_SETTINGS_SUCCESS,
    RESET_SETTINGS_FAIL,
    LOG_IN_SUCCESS,
    LOG_IN_FAIL,
    PASSWORD_STATUS
}