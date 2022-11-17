package sg.com.trekstorageauthentication.presentation.screen.register_pin

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import sg.com.trekstorageauthentication.R
import sg.com.trekstorageauthentication.presentation.component.LocalNavController
import sg.com.trekstorageauthentication.presentation.screen.register_pin.state.RegisterPinScreenStateHolder

@Composable
fun RegisterPinScreen() {
    rememberRegisterPinScreenStateHolder()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(modifier = Modifier.size(150.dp))

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = stringResource(R.string.setting_up_device),
            style = MaterialTheme.typography.h2,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun rememberRegisterPinScreenStateHolder(
    navController: NavHostController? = LocalNavController.current,
    context: Context = LocalContext.current,
    viewModel: RegisterPinViewModel = hiltViewModel(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
): RegisterPinScreenStateHolder {
    return remember {
        RegisterPinScreenStateHolder(navController, context, viewModel, coroutineScope)
    }
}