package sg.com.trekstorageauthentication.presentation

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import sg.com.trekstorageauthentication.presentation.navigation.NavGraph
import sg.com.trekstorageauthentication.presentation.ui.theme.TrekStorageAuthenticationTheme

@SuppressLint("SourceLockedOrientationActivity")
@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        //Restart activity on process death
        if (savedInstanceState != null) {
            val id = savedInstanceState.getInt(viewModel.viewModelId.first)
            if (id != viewModel.viewModelId.second) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        setContent {
            TrekStorageAuthenticationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    NavGraph(rememberNavController())
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(viewModel.viewModelId.first, viewModel.viewModelId.second)
    }
}