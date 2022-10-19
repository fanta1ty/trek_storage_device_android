package sg.com.trekstorageauthentication.presentation.state

import androidx.compose.material.ScaffoldState
import androidx.navigation.NavHostController
import sg.com.trekstorageauthentication.presentation.MainViewModel

class MainStateHolder(
    private val viewModel: MainViewModel,
    val scaffoldState: ScaffoldState,
    val navController: NavHostController,
) {
    fun getSnackbarEvent() = viewModel.snackbarEvent

    suspend fun registerNavigationEvent() {
        viewModel.navigationEvent.collect { event ->
            val (route, popUpToRoute, isInclusive) = event
            if (route.isEmpty()) { //Navigate back
                navController.navigateUp()
                return@collect
            }

            if (popUpToRoute.isEmpty()) {
                navController.navigate(route)
            } else {
                navController.navigate(route) {
                    popUpTo(popUpToRoute) { inclusive = isInclusive }
                }
            }
        }
    }
}