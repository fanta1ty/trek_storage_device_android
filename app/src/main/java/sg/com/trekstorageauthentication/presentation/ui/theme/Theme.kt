package sg.com.trekstorageauthentication.presentation.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val LightColorPalette = lightColors(
    primary = Blue500,
    primaryVariant = Blue200,
    onPrimary = Color.White,
    background = Color.White,
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black,
)

@Composable
fun TrekStorageAuthenticationTheme(
    content: @Composable () -> Unit
) {
    val colors = LightColorPalette
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(color = Blue700)

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}