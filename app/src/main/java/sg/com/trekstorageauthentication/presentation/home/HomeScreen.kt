package sg.com.trekstorageauthentication.presentation.home

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import sg.com.trekstorageauthentication.R
import sg.com.trekstorageauthentication.presentation.MainViewModel
import sg.com.trekstorageauthentication.presentation.home.component.HomeDialog
import sg.com.trekstorageauthentication.presentation.home.state.HomeScreenStateHolder
import sg.com.trekstorageauthentication.presentation.home.state.HomeState

@Composable
fun HomeScreen() {
    val stateHolder = rememberHomeScreenStateHolder()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.logo_login),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        )

        Button(
            onClick = stateHolder::showConfirmLogOutDialog,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) { Text(stringResource(R.string.log_out)) }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = stateHolder::showConfirmResetSettingsDialog,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) { Text(stringResource(R.string.reset_password)) }

        HomeDialog(
            stateHolder.homeState,
            stateHolder::logOut,
            stateHolder::resetSettings,
            stateHolder::resetHomeState
        )
    }
}

@Composable
private fun rememberHomeScreenStateHolder(
    context: Context = LocalContext.current,
    viewModel: MainViewModel = hiltViewModel(context as FragmentActivity),
    homeState: MutableState<HomeState> = mutableStateOf(HomeState())
): HomeScreenStateHolder {
    return remember { HomeScreenStateHolder(viewModel, homeState) }
}