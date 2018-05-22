package it.codingjam.coroutines

import android.arch.lifecycle.ViewModel
import it.codingjam.common.Badge
import it.codingjam.common.Tag
import it.codingjam.common.UserStats
import it.codingjam.common.arch.LiveDataDelegate
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.UI

class ViewModel3(private val service: StackOverflowServiceCoroutines) : ViewModel() {

    val liveDataDelegate = LiveDataDelegate("")

    var state by liveDataDelegate

    private val job = Job()

    fun load() {
        launch(CommonPool + job) {
            try {
                val users = service.getTopUsers().await()
                val firstUser = users.first()
                val badges: Deferred<List<Badge>> = service.getBadges(firstUser.id)
                val tags: Deferred<List<Tag>> = service.getTags(firstUser.id)
                val userStats = UserStats(firstUser, tags.await(), badges.await())
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