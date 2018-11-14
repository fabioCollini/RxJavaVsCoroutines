package it.codingjam.coroutines

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import java.util.*
import java.util.concurrent.TimeUnit.SECONDS

class ViewModel6(private val service: StackOverflowServiceCoroutines) : ViewModel() {

    val state = MutableLiveData<String>()

    fun load() {
        viewModelScope.launch {
            try {
                exponentialBackoff(3) {
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

suspend fun <T> exponentialBackoff(
        times: Int = 5,
        block: suspend () -> T): T {
    var currentDelay = 100L + Random().nextInt(100)
    repeat(times - 1) {
        try {
            return block()
        } catch (e: Exception) {
        }
        delay(currentDelay)
        currentDelay *= 2
    }
    return block()
}