package sg.com.trekstorageauthentication.presentation.screen.auth_success.state

import android.content.Context
import android.widget.Toast
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import sg.com.trekstorageauthentication.R
import sg.com.trekstorageauthentication.presentation.navigation.Screen
import sg.com.trekstorageauthentication.presentation.screen.auth_success.AuthSuccessViewModel
import sg.com.trekstorageauthentication.service.ble.BleResponseType

class AuthSuccessScreenStateHolder(
    private val navController: NavHostController?,
    private val context: Context,
    val viewModel: AuthSuccessViewModel,
    private val coroutineScope: CoroutineScope
) {
    init {
        registerDataResponseEvent()
    }

    fun navigateChangePinScreen() {
        navController?.navigate(Screen.RegisterPinScreen.withArgs(false))
    }

    private fun registerDataResponseEvent() {
        coroutineScope.launch {
            viewModel.getDataResponseEvent().collect {
                val (type, _) = it

                when (type) {
                    BleResponseType.RESET_SETTINGS_SUCCESS -> {
                        val msg = context.getString(R.string.reset_thumb_drive_successful)
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()

                        navController?.navigate(Screen.DeviceSelectionScreen.route) {
                            popUpTo(Screen.AuthSuccessScreen.route) { inclusive = true }
                        }
                    }

                    BleResponseType.RESET_SETTINGS_FAIL -> {
                        val msg = context.getString(R.string.reset_thumb_drive_fail)
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    }

                    else -> Unit
                }
            }
        }
    }
}