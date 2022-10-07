package sg.com.trekstorageauthentication.presentation

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarHost
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import sg.com.trekstorageauthentication.presentation.main.component.MainEventRegisterHandler
import sg.com.trekstorageauthentication.presentation.main.component.MainSnackbar
import sg.com.trekstorageauthentication.presentation.main.component.MainSnackbarHandler
import sg.com.trekstorageauthentication.presentation.main.state.MainStateHolder
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
                            snackbar = { data -> MainSnackbar(data.message) }
                        )
                    },
                ) {
                    MainEventRegisterHandler(stateHolder::registerNavigationEvent)

                    MainSnackbarHandler(stateHolder.getSnackbarEvent(), stateHolder.scaffoldState)

                    NavGraph(stateHolder.navController)
                }
            }
        }
    }
}

@Composable
private fun rememberMainStateHolder(
    viewModel: MainViewModel = hiltViewModel(),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    navController: NavHostController = rememberNavController(),
): MainStateHolder {
    return remember { MainStateHolder(viewModel, scaffoldState, navController) }
}