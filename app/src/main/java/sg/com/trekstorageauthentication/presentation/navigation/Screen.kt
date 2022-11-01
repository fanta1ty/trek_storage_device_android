package sg.com.trekstorageauthentication.presentation.navigation

sealed class Screen(val route: String) {
    object DeviceSelectionScreen : Screen("device_selection_screen")
    object AuthSuccessScreen : Screen("auth_success_screen")
    object AuthFailureScreen : Screen("auth_failure_screen")
    object RecoveryScreen : Screen("recovery_screen")
    object RegisterPinScreen : Screen("register_pin_screen") {
        const val isRegisterParam = "isRegister"
        val routeWithArgs = "$route?$isRegisterParam={$isRegisterParam}"

        fun withArgs(isRegister: Boolean) = "$route?$isRegisterParam=$isRegister"
    }
}