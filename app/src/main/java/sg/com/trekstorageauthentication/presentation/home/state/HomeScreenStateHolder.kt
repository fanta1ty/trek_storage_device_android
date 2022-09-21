package sg.com.trekstorageauthentication.presentation.home.state

import androidx.compose.runtime.MutableState
import sg.com.trekstorageauthentication.presentation.MainViewModel

class HomeScreenStateHolder(
    private val viewModel: MainViewModel,
    val homeState: MutableState<HomeState>
) {
    fun logOut() {
        resetHomeState()
        viewModel.logOut()
    }

    fun resetSettings() {
        resetHomeState()
    }

    fun showConfirmLogOutDialog() {
        homeState.value = homeState.value.copy(isShowConfirmLogOutDialog = true)
    }

    fun showConfirmResetSettingsDialog() {
        homeState.value = homeState.value.copy(isShowConfirmResetSettingsDialog = true)
    }

    fun resetHomeState() {
        homeState.value = HomeState()
    }
}