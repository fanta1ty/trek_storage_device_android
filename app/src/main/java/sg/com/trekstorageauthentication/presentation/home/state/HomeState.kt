package sg.com.trekstorageauthentication.presentation.home.state

data class HomeState(
    val isShowConfirmResetSettingsDialog: Boolean = false,
    val isShowConfirmLogOutDialog: Boolean = false
)