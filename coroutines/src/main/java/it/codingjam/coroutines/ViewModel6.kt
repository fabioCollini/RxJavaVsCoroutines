package it.codingjam.coroutines

import android.arch.lifecycle.ViewModel
import it.codingjam.common.arch.LiveDataDelegate
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.Dispatchers.Main
import java.util.*
import java.util.concurrent.TimeUnit.SECONDS
import kotlin.coroutines.experimental.CoroutineContext

class ViewModel6(private val service: StackOverflowServiceCoroutines) : ViewModel(), CoroutineScope {

    val liveDataDelegate = LiveDataDelegate("")

    var state by liveDataDelegate

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job

    fun load() {
        launch {
            try {
                exponentialBackoff(3) {
                    withTimeout(10, SECONDS) {
                        val users = service.getTopUsers().await()
                        updateUi(users)
                    }
                }
            } catch (e: Exception) {
                updateUi(e)
            }
        }
    }

    private suspend fun updateUi(s: Any) = withContext(Main) {
        state = s.toString()
    }


    override fun onCleared() {
        job.cancel()
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