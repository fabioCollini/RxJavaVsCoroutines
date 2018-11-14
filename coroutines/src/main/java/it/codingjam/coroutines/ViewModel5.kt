package it.codingjam.coroutines

import it.codingjam.common.arch.LiveDataDelegate
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import java.util.concurrent.TimeUnit.SECONDS

class ViewModel5(private val service: StackOverflowServiceCoroutines) : ViewModel() {

    val liveDataDelegate = LiveDataDelegate("")

    var state by liveDataDelegate

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
        state = s.toString()
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