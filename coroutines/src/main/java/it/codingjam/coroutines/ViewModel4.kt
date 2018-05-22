package it.codingjam.coroutines

import android.arch.lifecycle.ViewModel
import it.codingjam.common.UserStats
import it.codingjam.common.arch.LiveDataDelegate
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext

class ViewModel4(private val service: StackOverflowServiceCoroutines) : ViewModel() {

    val liveDataDelegate = LiveDataDelegate("")

    var state by liveDataDelegate

    private val job = Job()

    fun load() {
        launch(CommonPool + job) {
            try {
                val users = service.getTopUsers().await()
                val userStats: List<UserStats> =
                        users.take(5)
                                .map { Triple(it, service.getTags(it.id), service.getBadges(it.id)) }
                                .map { (user, tags, badges) -> UserStats(user, tags.await(), badges.await()) }
                updateUi(userStats)
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