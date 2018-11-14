package it.codingjam.coroutines

import it.codingjam.common.arch.LiveDataDelegate
import kotlinx.coroutines.launch

class ViewModel0(
        private val service: StackOverflowServiceCoroutines
) : ViewModel() {

    val liveDataDelegate = LiveDataDelegate("")

    var state by liveDataDelegate

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
        state = s.toString()
    }
}