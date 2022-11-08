package sg.com.trekstorageauthentication.presentation.screen.device_selection.state

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import sg.com.trekstorageauthentication.R
import sg.com.trekstorageauthentication.presentation.navigation.Screen
import sg.com.trekstorageauthentication.presentation.screen.device_selection.DeviceSelectionViewModel
import sg.com.trekstorageauthentication.service.authentication.AuthenticationService
import sg.com.trekstorageauthentication.service.authentication.AuthenticationServiceImpl
import sg.com.trekstorageauthentication.service.ble.BleResponseType

@OptIn(ExperimentalPermissionsApi::class)
class DeviceSelectionScreenStateHolder(
    private val context: Context,
    val viewModel: DeviceSelectionViewModel,
    private val navController: NavHostController?,
    private val coroutineScope: CoroutineScope,
    private val multiplePermissionsState: MultiplePermissionsState
) : AuthenticationService by AuthenticationServiceImpl(context) {

    private var activityResultLauncher: ActivityResultLauncher<Intent>? = null
    private var selectedItemPosition = -1

    init {
        registerDataResponseEvent()
    }

    fun toggleScanningOnOff() {
        if (!viewModel.getIsScanningState().value) { //Start scan
            if (multiplePermissionsState.allPermissionsGranted) {
                viewModel.startScan(true)
            } else {
                multiplePermissionsState.launchMultiplePermissionRequest()
            }
        } else { //Stop scan
            viewModel.stopScan()
        }
    }

    fun connect() {
        if (selectedItemPosition != -1) viewModel.connect(selectedItemPosition)
    }

    fun launchPasscodeAuthentication() {
        activityResultLauncher?.launch(getPasscodeAuthenticationIntent())
    }

    fun navigateToAppPermissionSettings() {
        viewModel.dismissDialog()

        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.parse("package:${context.packageName}")
        )
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    fun navigateToLocationServiceSettings() {
        viewModel.dismissDialog()

        if (!viewModel.isLocationServiceEnabled()) {
            context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        } else {
            viewModel.startScan(true)
        }
    }

    fun setActivityResultLauncher(activityResultLauncher: ActivityResultLauncher<Intent>) {
        this.activityResultLauncher = activityResultLauncher
    }

    fun setSelectedItemPosition(position: Int) {
        selectedItemPosition = position
    }

    private fun registerDataResponseEvent() {
        coroutineScope.launch {
            viewModel.getDataResponseEvent().collect {
                val (type, data) = it
                when (type) {
                    BleResponseType.PHONE_NAME_SENT -> viewModel.readPinStatus()

                    BleResponseType.PIN_STATUS -> {
                        when (String(data).toInt()) {
                            0 -> {
                                //USB already have a pin
                                viewModel.login()
                            }

                            1 -> { //USB doesn't have a pin
                                navController?.navigate(Screen.RegisterPinScreen.route) {
                                    popUpTo(Screen.DeviceSelectionScreen.route) { inclusive = true }
                                }
                            }

                            else -> { //Not Trek device
                                val msg = context.getString(R.string.no_trek_devices_found)
                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                    BleResponseType.LOG_IN_SUCCESS -> {
                        navController?.navigate(Screen.AuthSuccessScreen.route) {
                            popUpTo(Screen.DeviceSelectionScreen.route) { inclusive = true }
                        }
                    }

                    BleResponseType.LOG_IN_FAIL -> {
                        navController?.navigate(Screen.AuthFailureScreen.route) {
                            popUpTo(Screen.DeviceSelectionScreen.route) { inclusive = true }
                        }
                    }

                    else -> Unit
                }
            }
        }
    }
}