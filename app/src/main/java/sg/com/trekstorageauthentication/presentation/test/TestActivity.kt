package sg.com.trekstorageauthentication.presentation.test

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import sg.com.trekstorageauthentication.presentation.main.state.SnackbarEvent

@AndroidEntryPoint
class TestActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: TestViewModel = hiltViewModel()
            val scaffoldState = rememberScaffoldState()

            Scaffold(
                scaffoldState = scaffoldState,
                snackbarHost = { hostState ->
                    SnackbarHost(
                        hostState = hostState,
                        snackbar = { _ -> Snackbar { Text("This is Snackbar") } }
                    )
                }
            ) {
//                val snackbarEvent = viewModel.snackbarEvent.collectAsState(SnackbarEvent("0"))
//                LaunchedEffect(key1 = snackbarEvent.value) {
//                    Log.e("HuyTest", "snackbarEvent collect")
//                    scaffoldState.snackbarHostState.showSnackbar(
//                        "",
//                        duration = SnackbarDuration.Indefinite
//                    )
//                }

                val coroutineScope = rememberCoroutineScope()

                LaunchedEffect(key1 = true) {
                    viewModel.countChannel.collect {
                        Log.e("HuyTest", "1: $it")
                    }

                    viewModel.countChannel2.collect {
                        Log.e("HuyTest", "2: $it")
                    }
                }

                Button(onClick = {
                    viewModel.increase1()
                    viewModel.increase2()
                }) {
                    Text("Increase Counter")
                }
            }
        }
    }
}