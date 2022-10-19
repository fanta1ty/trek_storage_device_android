package sg.com.trekstorageauthentication.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import sg.com.trekstorageauthentication.presentation.screen.device_selection.DeviceSelectionScreen
import sg.com.trekstorageauthentication.presentation.screen.recovery.ResetScreen
import sg.com.trekstorageauthentication.presentation.screen.register_password.RegisterPasswordScreen

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