package it.codingjam.coroutines

import android.arch.lifecycle.ViewModel
import it.codingjam.common.UserStats
import it.codingjam.common.arch.LiveDataDelegate
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.Dispatchers.Main
import kotlin.coroutines.experimental.CoroutineContext

class ViewModel2(private val service: StackOverflowServiceCoroutines) : ViewModel(), CoroutineScope {

    val liveDataDelegate = LiveDataDelegate("")

    var state by liveDataDelegate

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job

    fun load() {
        launch {
            try {
                val users = service.getTopUsers().await()
                val usersWithBadges: List<UserStats> =
                        users.take(5)
                                .map { it to service.getBadges(it.id) }
                                .map { (user, badges) ->
                                    UserStats(user, badges.await())
                                }
                updateUi(usersWithBadges)
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