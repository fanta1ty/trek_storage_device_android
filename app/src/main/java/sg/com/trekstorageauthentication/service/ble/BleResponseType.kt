package sg.com.trekstorageauthentication.service.ble

enum class BleResponseType {
    REGISTER_PIN_SUCCESS,
    REGISTER_PIN_FAIL,
    LOG_IN_SUCCESS,
    LOG_IN_FAIL,
    DISABLE_AUTHENTICATION_SUCCESS,
    DISABLE_AUTHENTICATION_FAILED,
    FACTORY_RESET_SETTINGS_SUCCESS,
    FACTORY_RESET_SETTINGS_FAIL,
    PIN_STATUS,
    PHONE_NAME_SENT,
    PC_CONNECTION_STATUS
}