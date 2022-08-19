package sg.com.trekstorageauthentication.presentation.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

//private val DarkColorPalette = darkColors(
//    primary = Blue500,
//    primaryVariant = Blue200,
//    onPrimary = Color.White,
//    background = Color.DarkGray,
//    onBackground = Color.White,
//    surface = Color.DarkGray,
//    onSurface = Color.White
//)

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
    //darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
//    val colors = if (darkTheme) {
//        DarkColorPalette
//    } else {
//        LightColorPalette
//    }

    val colors = LightColorPalette

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}