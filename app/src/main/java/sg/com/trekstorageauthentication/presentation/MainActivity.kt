@file:OptIn(ExperimentalPermissionsApi::class)

package sg.com.trekstorageauthentication.presentation

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarHost
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import sg.com.trekstorageauthentication.presentation.main.component.MainDialog
import sg.com.trekstorageauthentication.presentation.main.component.MainLifecycleHandler
import sg.com.trekstorageauthentication.presentation.main.component.MainSnackbar
import sg.com.trekstorageauthentication.presentation.main.component.MainSnackbarHandler
import sg.com.trekstorageauthentication.presentation.main.state.MainStateHolder
import sg.com.trekstorageauthentication.presentation.main.state.SnackbarEvent
import sg.com.trekstorageauthentication.presentation.ui.navigation.NavGraph
import sg.com.trekstorageauthentication.presentation.ui.theme.TrekStorageAuthenticationTheme

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = Configuration.ORIENTATION_PORTRAIT

        setContent {
            TrekStorageAuthenticationTheme {
                val stateHolder = rememberMainStateHolder()

                Scaffold(
                    scaffoldState = stateHolder.scaffoldState,
                    snackbarHost = { hostState ->
                        SnackbarHost(
                            hostState = hostState,
                            snackbar = { data -> MainSnackbar(data.message, stateHolder) }
                        )
                    },
                ) {
                    val coroutineScope = rememberCoroutineScope()
                    Log.e("HuyTest", "Scaffold recompose")

                    LaunchedEffect(key1 = true) {
                        coroutineScope.launch {
                            stateHolder.registerBiometricAuthEvent()
                        }

                        coroutineScope.launch {
                            stateHolder.registerNavigationEvent()
                        }
                    }

                    MainLifecycleHandler(stateHolder)

                    MainSnackbarHandler(
                        stateHolder.getSnackbarEvent().collectAsState(SnackbarEvent("")),
                        stateHolder.scaffoldState
                    )

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
        MainStateHolder(
            context,
            viewModel,
            scaffoldState,
            navController,
            multiplePermissionsState
        )
    }
}