package sg.com.trekstorageauthentication.presentation

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


@HiltViewModel
@SuppressLint("StaticFieldLeak")
class MainViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {
    val viewModelId = Pair(
        "${context.packageName}:viewModel",
        System.currentTimeMillis().hashCode()
    )
}