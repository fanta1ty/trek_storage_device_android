package sg.com.trekstorageauthentication.presentation.screen.device_selection.state

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import sg.com.trekstorageauthentication.presentation.screen.device_selection.DeviceSelectionViewModel
import sg.com.trekstorageauthentication.service.authentication.AuthenticationService
import sg.com.trekstorageauthentication.service.authentication.AuthenticationServiceImpl

@OptIn(ExperimentalPermissionsApi::class)
class DeviceSelectionScreenStateHolder(
    private val context: Context,
    val viewModel: DeviceSelectionViewModel,
    private val multiplePermissionsState: MultiplePermissionsState
) : AuthenticationService by AuthenticationServiceImpl(context) {

    init {
        Log.d("HuyTest", "DeviceSelectionScreenStateHolder init")
    }

    private var activityResultLauncher: ActivityResultLauncher<Intent>? = null
    private var selectedItemPosition = -1

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
}