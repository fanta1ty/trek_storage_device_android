@file:OptIn(ExperimentalPermissionsApi::class)

package sg.com.trekstorageauthentication.presentation

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarHost
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import dagger.hilt.android.AndroidEntryPoint
import sg.com.trekstorageauthentication.presentation.main.component.*
import sg.com.trekstorageauthentication.presentation.main.state.MainStateHolder
import sg.com.trekstorageauthentication.presentation.ui.navigation.NavGraph
import sg.com.trekstorageauthentication.presentation.ui.theme.TrekStorageAuthenticationTheme

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    private lateinit var passcodeActivityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = Configuration.ORIENTATION_PORTRAIT
        registerPasscodeActivityResultLauncher()

        setContent {
            TrekStorageAuthenticationTheme {
                val stateHolder = rememberMainStateHolder()

                Scaffold(
                    scaffoldState = stateHolder.scaffoldState,
                    snackbarHost = { hostState ->
                        SnackbarHost(
                            hostState = hostState,
                            snackbar = { data ->
                                MainSnackbar(data.message, stateHolder::connectBle)
                            }
                        )
                    },
                ) {
                    MainLifecycleHandler(stateHolder::connectBle)

                    MainEventRegisterHandler(stateHolder::registerNavigationEvent)

                    MainSnackbarHandler(stateHolder.getSnackbarEvent(), stateHolder.scaffoldState)

                    NavGraph(stateHolder.navController)

                    MainDialog(
                        state = stateHolder.getMainState(),
                        stateHolder::navigateToAppPermissionSettings,
                        stateHolder::navigateToLocationServiceSettings,
                        stateHolder::dismissDialog
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        passcodeActivityResultLauncher.unregister()
        super.onDestroy()
    }

    private fun registerPasscodeActivityResultLauncher() {

    }
}

@Composable
private fun rememberMainStateHolder(
    context: Context = LocalContext.current,
    viewModel: MainViewModel = hiltViewModel(),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    navController: NavHostController = rememberNavController(),
    multiplePermissionsState: MultiplePermissionsState = rememberMultiplePermissionsState(
        permissions = viewModel.getRequiredPermissions(),
        onPermissionsResult = { viewModel.apply { connectBle(getPermissionResult(it)) } }
    )
): MainStateHolder {
    return remember {
        MainStateHolder(context, viewModel, scaffoldState, navController, multiplePermissionsState)
    }
}