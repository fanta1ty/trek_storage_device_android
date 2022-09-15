package sg.com.trekstorageauthentication.presentation.home.state

import android.content.Context
import sg.com.trekstorageauthentication.presentation.MainViewModel
import sg.com.trekstorageauthentication.presentation.ui.navigation.Screen

class HomeScreenStateHolder(
    private val context: Context,
    private val viewModel: MainViewModel
) {
    fun logOut(){
        viewModel.logOut()
    }

    fun navigateResetPasswordScreen() {
        viewModel.navigate(Screen.ResetPasswordScreen.route)
    }
}