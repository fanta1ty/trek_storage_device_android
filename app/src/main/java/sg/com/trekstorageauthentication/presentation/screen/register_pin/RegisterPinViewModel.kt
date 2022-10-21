package sg.com.trekstorageauthentication.presentation.screen.register_pin

import androidx.lifecycle.ViewModel
import sg.com.trekstorageauthentication.common.Constants
import sg.com.trekstorageauthentication.service.ble.BleService
import sg.com.trekstorageauthentication.service.datastore.DataStoreService
import sg.com.trekstorageauthentication.service.datastore.DataStoreServiceImpl
import javax.inject.Inject

class RegisterPinViewModel @Inject constructor(
    private val bleService: BleService
) : ViewModel(), DataStoreService by DataStoreServiceImpl() {

    fun registerPin(pin: String) {
        bleService.write(Constants.REGISTER_PIN_CHARACTERISTIC_UUID, pin.toByteArray())
    }

    fun getDataResponseEvent() = bleService.getDataResponseEvent()
}