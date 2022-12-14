package sg.com.trekstorageauthentication.common

object Constants {
    //Data Store
    const val DATA_STORE_NAME = "Trek_Data_Store"
    const val DATA_STORE_PIN_KEY = "Pin"

    //BLE
    const val SERVICE_UUID = "a4ff2a50-28de-11ed-a261-0242ac120002"
    const val NOTIFICATION_CHARACTERISTIC_UUID = "a4ff2a51-28de-11ed-a261-0242ac120002"
    const val NOTIFICATION_DESCRIPTOR_UUID = "00002902-0000-1000-8000-00805f9b34fb"
    const val READ_PIN_STATUS_CHARACTERISTIC_UUID = "a4ff2a52-28de-11ed-a261-0242ac120002"
    const val REGISTER_PIN_CHARACTERISTIC_UUID = "a4ff2a53-28de-11ed-a261-0242ac120002"
    const val LOG_IN_CHARACTERISTIC_UUID = "a4ff2a54-28de-11ed-a261-0242ac120002"
    const val DISABLE_AUTHENTICATION_CHARACTERISTIC_UUID = "a4ff2a56-28de-11ed-a261-0242ac120002"
    const val FACTORY_RESET_CHARACTERISTIC_UUID = "a4ff2a57-28de-11ed-a261-0242ac120002"
    const val SEND_PHONE_NAME_UUID = "a4ff2a58-28de-11ed-a261-0242ac120002"
    const val READ_PC_CONNECTION_STATUS_UUID = "a4ff2a59-28de-11ed-a261-0242ac120002"
}