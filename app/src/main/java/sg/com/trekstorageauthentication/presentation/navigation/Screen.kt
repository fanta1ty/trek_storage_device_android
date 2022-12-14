package sg.com.trekstorageauthentication.presentation.navigation

sealed class Screen(val route: String) {
    object DeviceSelectionScreen : Screen("device_selection_screen")
    object AuthSuccessScreen : Screen("auth_success_screen") {
        const val isRegisterParam = "isRegister"
        val routeWithArgs = "$route?$isRegisterParam={$isRegisterParam}"

        fun withArgs(isRegister: Boolean) = "$route?$isRegisterParam=$isRegister"
    }

    object AuthFailureScreen : Screen("auth_failure_screen")
    object RegisterPinScreen : Screen("register_pin_screen")
    object DisableAuthenticationSuccessScreen : Screen("disable_authentication_success_screen")
    object DisableAuthenticationFailedScreen : Screen("disable_authentication_failed_screen")
    object FactoryResetSuccessScreen : Screen("factory_reset_success_screen")
    object FactoryResetFailedScreen : Screen("factory_reset_failed_screen")
}