package it.codingjam.coroutines

import android.arch.lifecycle.ViewModel
import it.codingjam.common.arch.LiveDataDelegate
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import java.util.concurrent.TimeUnit.SECONDS
import kotlin.coroutines.CoroutineContext

class ViewModel5(private val service: StackOverflowServiceCoroutines) : ViewModel(), CoroutineScope {

    val liveDataDelegate = LiveDataDelegate("")

    var state by liveDataDelegate

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job

    fun load() {
        launch {
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

    private suspend fun updateUi(s: Any) = withContext(Main) {
        state = s.toString()
    }


    override fun onCleared() {
        job.cancel()
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