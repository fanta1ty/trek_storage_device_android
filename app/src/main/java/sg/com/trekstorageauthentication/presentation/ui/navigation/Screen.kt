package sg.com.trekstorageauthentication.presentation.ui.navigation

sealed class Screen(val route: String) {
    object UnlockScreen : Screen("unlock_screen")
    object ResetPasswordScreen : Screen("reset_password_screen")
    object RegisterPasswordScreen : Screen("register_password_screen")
}