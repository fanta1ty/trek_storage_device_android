package sg.com.trekstorageauthentication.presentation.screen.recovery

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import sg.com.trekstorageauthentication.R
import sg.com.trekstorageauthentication.common.Constants
import sg.com.trekstorageauthentication.service.ble.BleService
import sg.com.trekstorageauthentication.service.datastore.DataStoreService
import sg.com.trekstorageauthentication.service.datastore.DataStoreServiceImpl
import javax.inject.Inject

@HiltViewModel
@SuppressLint("StaticFieldLeak")
class RecoveryViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val bleService: BleService
) : ViewModel(), DataStoreService by DataStoreServiceImpl() {

    fun login(pin: String) {
        if (bleService.isConnected()) {
            bleService.write(Constants.LOG_IN_CHARACTERISTIC_UUID, pin.toByteArray())
        } else {
            val msg = context.getString(R.string.bluetooth_disconnected)
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }

    fun getDataResponseEvent() = bleService.getDataResponseEvent()
}