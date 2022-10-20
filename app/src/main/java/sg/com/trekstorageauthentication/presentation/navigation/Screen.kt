package sg.com.trekstorageauthentication.presentation.navigation

sealed class Screen(val route: String) {
    object DeviceSelectionScreen : Screen("device_selection_screen")
    object AuthSuccessScreen : Screen("auth_success_screen")
    object AuthFailureScreen : Screen("auth_failure_screen")
    object RegisterPasswordScreen : Screen("register_password_screen")
    object ResetScreen : Screen("reset_screen")
}