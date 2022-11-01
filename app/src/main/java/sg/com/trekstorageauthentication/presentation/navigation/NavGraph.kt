package sg.com.trekstorageauthentication.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import sg.com.trekstorageauthentication.presentation.component.LocalNavController
import sg.com.trekstorageauthentication.presentation.screen.auth_failure.AuthFailureScreen
import sg.com.trekstorageauthentication.presentation.screen.auth_success.AuthSuccessScreen
import sg.com.trekstorageauthentication.presentation.screen.device_selection.DeviceSelectionScreen
import sg.com.trekstorageauthentication.presentation.screen.recovery.RecoveryScreen
import sg.com.trekstorageauthentication.presentation.screen.register_pin.RegisterPinScreen

@Composable
fun NavGraph(navController: NavHostController) {
    CompositionLocalProvider(LocalNavController provides navController) {
        NavHost(
            navController = navController,
            startDestination = Screen.DeviceSelectionScreen.route
        ) {
            composable(route = Screen.DeviceSelectionScreen.route) {
                DeviceSelectionScreen()
            }

            composable(route = Screen.AuthSuccessScreen.route) {
                AuthSuccessScreen()
            }

            composable(route = Screen.AuthFailureScreen.route) {
                AuthFailureScreen()
            }

            composable(
                route = Screen.RegisterPinScreen.routeWithArgs,
                arguments = listOf(navArgument(Screen.RegisterPinScreen.isRegisterParam) {
                    type = NavType.BoolType
                    defaultValue = true
                })
            ) { entry ->
                RegisterPinScreen(
                    entry.arguments!!.getBoolean(Screen.RegisterPinScreen.isRegisterParam)
                )
            }

            composable(route = Screen.RecoveryScreen.route) {
                RecoveryScreen()
            }
        }
    }
}