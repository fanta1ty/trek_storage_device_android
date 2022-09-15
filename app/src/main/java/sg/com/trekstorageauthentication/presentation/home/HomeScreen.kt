package sg.com.trekstorageauthentication.presentation.home

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import sg.com.trekstorageauthentication.R
import sg.com.trekstorageauthentication.presentation.MainViewModel
import sg.com.trekstorageauthentication.presentation.home.state.HomeScreenStateHolder

@Composable
fun HomeScreen() {
    val stateHolder = rememberHomeScreenStateHolder()

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
            .height(LocalConfiguration.current.screenHeightDp.dp)
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
            onClick = stateHolder::logOut,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) { Text(stringResource(R.string.log_out)) }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = stateHolder::navigateResetPasswordScreen,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) { Text(stringResource(R.string.reset_password)) }
    }
}

@Composable
private fun rememberHomeScreenStateHolder(
    context: Context = LocalContext.current,
    viewModel: MainViewModel = hiltViewModel(context as FragmentActivity)
): HomeScreenStateHolder {
    return remember { HomeScreenStateHolder(context, viewModel) }
}