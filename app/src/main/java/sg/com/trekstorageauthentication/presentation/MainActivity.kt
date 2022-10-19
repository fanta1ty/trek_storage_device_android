package sg.com.trekstorageauthentication.presentation

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarHost
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import sg.com.trekstorageauthentication.presentation.component.MainSnackbar
import sg.com.trekstorageauthentication.presentation.navigation.NavGraph
import sg.com.trekstorageauthentication.presentation.state.MainStateHolder
import sg.com.trekstorageauthentication.presentation.ui.theme.TrekStorageAuthenticationTheme

@SuppressLint("SourceLockedOrientationActivity")
@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

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
                ) { padding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                    ) { NavGraph(stateHolder.navController) }
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