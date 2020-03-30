package letsbuildthatapp.com.kotlinmessenger.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is the Home View Model"
    }
    val text: LiveData<String> = _text
}