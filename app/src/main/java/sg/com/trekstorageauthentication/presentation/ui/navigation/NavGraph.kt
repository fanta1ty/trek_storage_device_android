package sg.com.trekstorageauthentication.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import sg.com.trekstorageauthentication.presentation.device_selection.DeviceSelectionScreen
import sg.com.trekstorageauthentication.presentation.register_password.RegisterPasswordScreen
import sg.com.trekstorageauthentication.presentation.reset.ResetScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.DeviceSelectionScreen.route
    ) {
        composable(route = Screen.DeviceSelectionScreen.route) {
            DeviceSelectionScreen()
        }

        composable(route = Screen.RegisterPasswordScreen.route) {
            RegisterPasswordScreen()
        }

        composable(route = Screen.ResetScreen.route) {
            ResetScreen()
        }
    }
}