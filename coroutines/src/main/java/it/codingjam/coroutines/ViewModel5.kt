package it.codingjam.coroutines

import android.arch.lifecycle.ViewModel
import it.codingjam.common.arch.LiveDataDelegate
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.UI
import java.util.concurrent.TimeUnit.SECONDS

class ViewModel5(private val service: StackOverflowServiceCoroutines) : ViewModel() {

    val liveDataDelegate = LiveDataDelegate("")

    var state by liveDataDelegate

    private val job = Job()

    fun load() {
        launch(CommonPool + job) {
            try {
                retry(3) {
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

    private suspend fun updateUi(s: Any) = withContext(UI) {
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