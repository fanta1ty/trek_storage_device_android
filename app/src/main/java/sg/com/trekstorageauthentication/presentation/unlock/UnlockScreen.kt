package sg.com.trekstorageauthentication.presentation.unlock

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import sg.com.trekstorageauthentication.R
import sg.com.trekstorageauthentication.presentation.MainViewModel
import sg.com.trekstorageauthentication.presentation.unlock.state.UnlockScreenStateHolder

@Composable
fun UnlockScreen() {
    val stateHolder = rememberLoginScreenStateHolder()

    LaunchedEffect(key1 = true) { launch { stateHolder.registerBiometricAuthEvent() } }

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
            onClick = stateHolder::authenticate,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) { Text(stringResource(R.string.login)) }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = stateHolder::navigateResetScreen,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) { Text(stringResource(R.string.reset)) }
    }
}

@Composable
private fun rememberLoginScreenStateHolder(
    context: Context = LocalContext.current,
    viewModel: MainViewModel = hiltViewModel(context as FragmentActivity),
): UnlockScreenStateHolder {
    return remember { UnlockScreenStateHolder(context, viewModel) }
}