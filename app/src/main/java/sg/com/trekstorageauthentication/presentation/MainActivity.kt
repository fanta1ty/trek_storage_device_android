@file:OptIn(ExperimentalPermissionsApi::class)

package sg.com.trekstorageauthentication.presentation

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import sg.com.trekstorageauthentication.presentation.main.component.BluetoothConnectionHandler
import sg.com.trekstorageauthentication.presentation.main.component.PermissionDeniedDialog
import sg.com.trekstorageauthentication.presentation.main.state.MainStateHolder
import sg.com.trekstorageauthentication.presentation.ui.navigation.NavGraph
import sg.com.trekstorageauthentication.presentation.ui.theme.TrekStorageAuthenticationTheme

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = Configuration.ORIENTATION_PORTRAIT

        setContent {
            val stateHolder = rememberMainStateHolder()

            BluetoothConnectionHandler(action = stateHolder::connectBle)

            TrekStorageAuthenticationTheme {
                Scaffold(scaffoldState = stateHolder.scaffoldState) {
                    NavGraph(rememberNavController())

                    PermissionDeniedDialog(
                        mainState = stateHolder.getMainState(),
                        onPositiveEvent = stateHolder::navigateToAppPermissionSettings
                    )
                }
            }
        }
    }
}

@Composable
private fun rememberMainStateHolder(
    context: Context = LocalContext.current,
    viewModel: MainViewModel = hiltViewModel(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    multiplePermissionsState: MultiplePermissionsState = rememberMultiplePermissionsState(
        permissions = viewModel.getRequiredPermissions(),
        onPermissionsResult = { viewModel.apply { connectBle(getPermissionResult(it)) } }
    )
): MainStateHolder {
    return remember {
        MainStateHolder(context, viewModel, coroutineScope, scaffoldState, multiplePermissionsState)
    }
}