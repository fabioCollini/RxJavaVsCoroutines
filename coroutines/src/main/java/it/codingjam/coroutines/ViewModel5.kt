package it.codingjam.coroutines

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import java.util.concurrent.TimeUnit.SECONDS

class ViewModel5(private val service: StackOverflowServiceCoroutines) : ViewModel() {

    val state = MutableLiveData<String>()

    fun load() {
        viewModelScope.launch {
            try {
                retry(3) {
                    withTimeout(SECONDS.toMillis(10)) {
                        val users = service.getTopUsers()
                        updateUi(users)
                    }
                }
            } catch (e: Exception) {
                updateUi(e)
            }
        }
    }

    private fun updateUi(s: Any) {
        state.value = s.toString()
    }
}

suspend fun <T> retry(attempts: Int = 5, f: suspend () -> T): T {
    repeat(attempts - 1) {
        try {
            f()
        } catch (e: Exception) {
        }
    }
    return f()
}