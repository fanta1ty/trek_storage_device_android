package sg.com.trekstorageauthentication.presentation.main.state

data class NavigationEvent(
    val route: String,
    val popUpToRoute: String = "",
    val inclusive: Boolean = false
)