package sg.com.trekstorageauthentication.presentation.main.state

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.State
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import sg.com.trekstorageauthentication.R
import sg.com.trekstorageauthentication.common.Constants
import sg.com.trekstorageauthentication.presentation.MainViewModel

@OptIn(ExperimentalPermissionsApi::class)
class MainStateHolder(
    private val context: Context,
    private val viewModel: MainViewModel,
    val scaffoldState: ScaffoldState,
    val navController: NavHostController,
    private val multiplePermissionsState: MultiplePermissionsState
) {
    fun connectBle() {
        if (multiplePermissionsState.allPermissionsGranted) {
            viewModel.connectBle(true)
        } else {
            multiplePermissionsState.launchMultiplePermissionRequest()
        }
    }

    fun navigateToAppPermissionSettings() {
        dismissDialog()
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.parse("package:${context.packageName}")
        )
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    fun navigateToLocationServiceSettings() {
        dismissDialog()

        if (!viewModel.isLocationServiceEnabled()) {
            context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        } else {
            viewModel.connectBle(true)
        }
    }

    fun getSnackbarEvent() = viewModel.snackbarEvent

    fun dismissDialog() {
        viewModel.resetMainState()
    }

    fun getMainState(): State<MainState> {
        return viewModel.mainState
    }

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

    suspend fun registerBiometricAuthEvent() {
        viewModel.biometricAuthEvent.collect {
            val isBiometricAuthenticationReady = isBiometricAuthenticationReady()
            if (!isBiometricAuthenticationReady) {
                viewModel.readBleData(Constants.READ_PASSWORD_STATUS_CHARACTERISTIC_UUID)
                return@collect
            }

            val promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle(context.getString(R.string.dialog_biometric_authentication_title))
                .setDescription(context.getString(R.string.dialog_biometric_authentication_description))
                .setNegativeButtonText(context.getString(android.R.string.cancel))
                .build()

            val executor = ContextCompat.getMainExecutor(context)

            BiometricPrompt(
                context as FragmentActivity,
                executor,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        viewModel.readBleData(Constants.READ_PASSWORD_STATUS_CHARACTERISTIC_UUID)
                    }
                }).apply { authenticate(promptInfo) }
        }
    }

    private fun isBiometricAuthenticationReady(): Boolean {
        val biometricManager = BiometricManager.from(context)

        return when (biometricManager.canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_STRONG or
                    BiometricManager.Authenticators.BIOMETRIC_WEAK
        )) {
            BiometricManager.BIOMETRIC_SUCCESS -> true
            else -> false
        }
    }
}