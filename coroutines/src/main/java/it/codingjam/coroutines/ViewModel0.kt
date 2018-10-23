package it.codingjam.coroutines

import android.arch.lifecycle.ViewModel
import it.codingjam.common.arch.LiveDataDelegate
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.Dispatchers.IO
import kotlinx.coroutines.experimental.Dispatchers.Main
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext

class ViewModel0(
        private val service: StackOverflowServiceCoroutines
) : ViewModel(), CoroutineScope {

    val liveDataDelegate = LiveDataDelegate("")

    var state by liveDataDelegate

    private val job = Job()

    override val coroutineContext
        get() = IO + job

    fun load() {
        launch {
            try {
                val users = service.getTopUsers().await()
                updateUi(users)
            } catch (e: Exception) {
                updateUi(e)
            }
        }
    }

    private suspend fun updateUi(s: Any) {
        withContext(Main) {
            //...
            state = s.toString()
        }
    }

    override fun onCleared() {
        job.cancel()
    }
}