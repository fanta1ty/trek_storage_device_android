package sg.com.trekstorageauthentication.presentation.screen.recovery

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import sg.com.trekstorageauthentication.common.Constants
import sg.com.trekstorageauthentication.service.ble.BleService
import sg.com.trekstorageauthentication.service.datastore.DataStoreService
import sg.com.trekstorageauthentication.service.datastore.DataStoreServiceImpl
import javax.inject.Inject

@HiltViewModel
class RecoveryViewModel @Inject constructor(
    private val bleService: BleService
) : ViewModel(), DataStoreService by DataStoreServiceImpl() {

    fun login(pin: String) {
        bleService.write(Constants.LOG_IN_CHARACTERISTIC_UUID, pin.toByteArray())
    }

    fun getDataResponseEvent() = bleService.getDataResponseEvent()
}