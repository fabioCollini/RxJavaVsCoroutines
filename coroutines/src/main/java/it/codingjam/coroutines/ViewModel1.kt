package it.codingjam.coroutines

import android.arch.lifecycle.ViewModel
import it.codingjam.common.arch.LiveDataDelegate
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async

class ViewModel1(private val service: StackOverflowServiceCoroutines) : ViewModel() {

    val liveDataDelegate = LiveDataDelegate("")

    var state by liveDataDelegate

    private val job = Job()

    fun load() {
        async(UI + job) {
            state = try {
                service.getTopUsers().await().toString()
            } catch (e: Exception) {
                e.toString()
            }
        }
    }

    override fun onCleared() {
        job.cancel()
    }
}