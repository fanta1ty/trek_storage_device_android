package sg.com.trekstorageauthentication.presentation

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.compose.setContent
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

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
}

//TODO: Process death
//test recompose