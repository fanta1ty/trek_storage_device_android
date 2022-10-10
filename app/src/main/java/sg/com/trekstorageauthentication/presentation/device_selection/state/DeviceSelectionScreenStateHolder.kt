package sg.com.trekstorageauthentication.presentation.device_selection.state

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import sg.com.trekstorageauthentication.presentation.device_selection.DeviceSelectionViewModel

@OptIn(ExperimentalPermissionsApi::class)
class DeviceSelectionScreenStateHolder(
    private val context: Context,
    val viewModel: DeviceSelectionViewModel,
    private val multiplePermissionsState: MultiplePermissionsState
) {
    fun startScan() {
        if (multiplePermissionsState.allPermissionsGranted) {
            viewModel.startScan(true)
        } else {
            multiplePermissionsState.launchMultiplePermissionRequest()
        }
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
}