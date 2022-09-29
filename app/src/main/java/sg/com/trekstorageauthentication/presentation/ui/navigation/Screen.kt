package sg.com.trekstorageauthentication.presentation.ui.navigation

sealed class Screen(val route: String) {
    object DeviceSelectionScreen : Screen("device_selection_screen")
    object RegisterPasswordScreen : Screen("register_password_screen")
    object UnlockScreen : Screen("unlock_screen")
    object HomeScreen : Screen("home_screen")
    object ResetScreen : Screen("reset_screen")
}