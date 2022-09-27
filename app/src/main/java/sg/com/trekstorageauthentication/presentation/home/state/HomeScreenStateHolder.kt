package sg.com.trekstorageauthentication.presentation.home.state

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import sg.com.trekstorageauthentication.presentation.MainViewModel

class HomeScreenStateHolder(
    private val viewModel: MainViewModel,
    private val _homeState: MutableState<HomeState>
) {
    val homeState: State<HomeState>
        get() = _homeState

    fun logOut() {
        resetHomeState()
        viewModel.logOut()
    }

    fun showConfirmLogOutDialog() {
        _homeState.value = homeState.value.copy(isShowConfirmLogOutDialog = true)
    }

    fun resetHomeState() {
        _homeState.value = HomeState()
    }
}