package sg.com.trekstorageauthentication.presentation.state

data class NavigationEvent(
    val route: String = "",
    val popUpToRoute: String = "",
    val inclusive: Boolean = true
)