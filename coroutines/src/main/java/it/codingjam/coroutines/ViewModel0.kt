package it.codingjam.coroutines

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.launch

class ViewModel0(
        private val service: StackOverflowServiceCoroutines
) : ViewModel() {

    val state = MutableLiveData<String>()

    fun load() {
        viewModelScope.launch {
            try {
                val users = service.getTopUsers()
                updateUi(users)
            } catch (e: Exception) {
                updateUi(e)
            }
        }
    }

    private fun updateUi(s: Any) {
        state.value = s.toString()
    }
}