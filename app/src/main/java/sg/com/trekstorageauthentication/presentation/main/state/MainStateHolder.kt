package sg.com.trekstorageauthentication.presentation.main.state

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarDuration
import androidx.compose.runtime.State
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import sg.com.trekstorageauthentication.R
import sg.com.trekstorageauthentication.presentation.MainViewModel
import sg.com.trekstorageauthentication.service.ble.BleConnectionState

@OptIn(ExperimentalPermissionsApi::class)
class MainStateHolder(
    private val context: Context,
    private val viewModel: MainViewModel,
    private val coroutineScope: CoroutineScope,
    val scaffoldState: ScaffoldState,
    private val multiplePermissionsState: MultiplePermissionsState
) {

    init {
        registerBleConnectionState()
    }

    fun connectBle() {
        if (multiplePermissionsState.allPermissionsGranted) {
            viewModel.connectBle(true)
        } else {
            multiplePermissionsState.launchMultiplePermissionRequest()
        }
    }

    private fun registerBleConnectionState() {
        coroutineScope.launch {
            viewModel.getBleConnectionState().collectLatest {
                when (it) {
                    BleConnectionState.CONNECTING -> {
                        scaffoldState.snackbarHostState.showSnackbar(
                            context.getString(R.string.connecting),
                            duration = SnackbarDuration.Indefinite
                        )
                    }

                    BleConnectionState.DISCONNECTED -> {
                        scaffoldState.snackbarHostState.showSnackbar(
                            context.getString(R.string.disconnected),
                            duration = SnackbarDuration.Indefinite
                        )
                    }

                    else -> {
                        scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
                    }
                }
            }
        }
    }

    fun navigateToAppPermissionSettings() {
        viewModel.resetMainState()

        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.parse("package:${context.packageName}")
        )
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    fun navigateToLocationServiceSettings() {
        context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
    }

    fun getMainState(): State<MainState> {
        return viewModel.mainState
    }
}