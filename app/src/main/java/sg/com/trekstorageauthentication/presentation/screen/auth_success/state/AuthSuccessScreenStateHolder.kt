package sg.com.trekstorageauthentication.presentation.screen.auth_success.state

import android.content.Context
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import sg.com.trekstorageauthentication.presentation.navigation.Screen
import sg.com.trekstorageauthentication.presentation.screen.auth_success.AuthSuccessViewModel
import sg.com.trekstorageauthentication.service.ble.BleResponseType

class AuthSuccessScreenStateHolder(
    val isRegister: Boolean,
    private val navController: NavHostController?,
    private val context: Context,
    val viewModel: AuthSuccessViewModel,
    private val coroutineScope: CoroutineScope
) {
    init {
        registerDataResponseEvent()
    }

    fun navigateChangePinScreen() {
        navController?.navigate(Screen.RegisterPinScreen.withArgs(false)) {
            popUpTo(Screen.AuthSuccessScreen.route) { inclusive = true }
        }
    }

    private fun registerDataResponseEvent() {
        coroutineScope.launch {
            viewModel.getDataResponseEvent().collect {
                val (type, _) = it

                when (type) {
                    BleResponseType.DISABLE_AUTHENTICATION_SUCCESS -> {
                        viewModel.thumbDriveDisablingAuthentication = false
                        navController?.navigate(Screen.DisableAuthenticationSuccessScreen.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }

                    BleResponseType.DISABLE_AUTHENTICATION_FAILED -> {
                        viewModel.thumbDriveDisablingAuthentication = false
                        navController?.navigate(Screen.DisableAuthenticationFailedScreen.route)
                    }

                    BleResponseType.FACTORY_RESET_SETTINGS_SUCCESS -> {
//                        val msg = context.getString(R.string.reset_thumb_drive_successful)
//                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()

                        viewModel.thumbDriveFactoryResetting = false
                        navController?.navigate(Screen.FactoryResetSuccessScreen.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }

                    BleResponseType.FACTORY_RESET_SETTINGS_FAIL -> {
//                        val msg = context.getString(R.string.reset_thumb_drive_fail)
//                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()

                        viewModel.thumbDriveFactoryResetting = false
                        navController?.navigate(Screen.FactoryResetFailedScreen.route)
                    }

                    else -> Unit
                }
            }
        }
    }
}