package it.codingjam.coroutines

import android.arch.lifecycle.ViewModel
import it.codingjam.common.arch.LiveDataDelegate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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