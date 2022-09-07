package sg.com.trekstorageauthentication.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import sg.com.trekstorageauthentication.presentation.register_password.RegisterPasswordScreen
import sg.com.trekstorageauthentication.presentation.reset_password.ResetPasswordScreen
import sg.com.trekstorageauthentication.presentation.unlock.UnlockScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.UnlockScreen.route
    ) {
        composable(route = Screen.UnlockScreen.route) {
            UnlockScreen(navController)
        }

        composable(route = Screen.RegisterPasswordScreen.route) {
            RegisterPasswordScreen()
        }

        composable(route = Screen.ResetPasswordScreen.route) {
            ResetPasswordScreen(navController)
        }
    }
}