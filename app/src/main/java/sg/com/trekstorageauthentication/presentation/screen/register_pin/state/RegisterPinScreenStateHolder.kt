package sg.com.trekstorageauthentication.presentation.screen.register_pin.state

import android.content.Context
import android.widget.Toast
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import sg.com.trekstorageauthentication.R
import sg.com.trekstorageauthentication.presentation.navigation.Screen
import sg.com.trekstorageauthentication.presentation.screen.register_pin.RegisterPinViewModel
import sg.com.trekstorageauthentication.service.ble.BleResponseType

class RegisterPinScreenStateHolder(
    private val navController: NavHostController?,
    private val context: Context,
    private val viewModel: RegisterPinViewModel,
    private val coroutineScope: CoroutineScope,
) {
    init {
        registerDataResponseEvent()
        setUpDevice()
    }

    private fun setUpDevice() {
        // Auto generate pin and register it
        coroutineScope.launch {
            // Add delay so that data response event is registered properly
            delay(2000)
            val pin = viewModel.getStoredPin(context)
            viewModel.registerPin(pin)
        }
    }

    private fun registerDataResponseEvent() {
        coroutineScope.launch {
            viewModel.getDataResponseEvent().collect {
                val (type, _) = it

                when (type) {
                    BleResponseType.REGISTER_PIN_SUCCESS -> {
                        val currentRoute = navController?.currentBackStackEntry?.destination?.route
                            ?: Screen.RegisterPinScreen.route

                        navController?.navigate(Screen.AuthSuccessScreen.withArgs(true)) {
                            popUpTo(currentRoute) { inclusive = true }
                        }
                    }

                    BleResponseType.REGISTER_PIN_FAIL -> {
                        val msg = context.getString(R.string.device_setup_failed)
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        navController?.popBackStack()
                    }

                    else -> Unit
                }
            }
        }
    }
}