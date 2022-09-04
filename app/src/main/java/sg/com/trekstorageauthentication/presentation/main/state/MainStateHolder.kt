package sg.com.trekstorageauthentication.presentation.main.state

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.State
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import sg.com.trekstorageauthentication.presentation.MainViewModel

@OptIn(ExperimentalPermissionsApi::class)
class MainStateHolder(
    private val context: Context,
    private val viewModel: MainViewModel,
    val scaffoldState: ScaffoldState,
    private val multiplePermissionsState: MultiplePermissionsState
) {
    fun connectBle() {
        if (multiplePermissionsState.allPermissionsGranted) {
            viewModel.connectBle(true)
        } else {
            multiplePermissionsState.launchMultiplePermissionRequest()
        }
    }

    fun navigateToAppPermissionSettings() {
        dismissDialog()
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.parse("package:${context.packageName}")
        )
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    fun navigateToLocationServiceSettings() {
        dismissDialog()

        if (!viewModel.isLocationServiceEnabled()) {
            context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
    }

    fun getSnackbarEvent() = viewModel.snackbarEvent

    fun dismissDialog() {
        viewModel.resetMainState()
    }

    fun getMainState(): State<MainState> {
        return viewModel.mainState
    }
}