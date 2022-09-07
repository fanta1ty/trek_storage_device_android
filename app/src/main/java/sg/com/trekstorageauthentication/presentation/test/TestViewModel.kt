package sg.com.trekstorageauthentication.presentation.test

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import sg.com.trekstorageauthentication.presentation.main.state.SnackbarEvent
import javax.inject.Inject

@HiltViewModel
class TestViewModel @Inject constructor() : ViewModel() {
    private var counter = 0
    private var counter2 = 0

    private val _countChannel = Channel<Int>()
    val countChannel = _countChannel.receiveAsFlow()

    private val _countChannel2 = Channel<Int>()
    val countChannel2 = _countChannel2.receiveAsFlow()

    private val _snackbarEvent = Channel<SnackbarEvent>()
    val snackbarEvent = _snackbarEvent.receiveAsFlow()

    fun increase1() {
        viewModelScope.launch {
            _countChannel.send(counter++)
        }
    }

    fun increase2() {
        viewModelScope.launch {
            _countChannel2.send(counter2++)
        }
    }

    fun showSnackbar() {
        viewModelScope.launch {
            Log.e("HuyTest", "showSnackbar Start")
            _snackbarEvent.send(SnackbarEvent("${++counter}"))
            Log.e("HuyTest", "showSnackbar End")
        }
    }
}