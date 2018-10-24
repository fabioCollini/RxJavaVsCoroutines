package it.codingjam.coroutines

import android.arch.lifecycle.ViewModel
import it.codingjam.common.User
import it.codingjam.common.UserStats
import it.codingjam.common.arch.LiveDataDelegate
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import kotlin.coroutines.CoroutineContext

class ViewModel4(private val service: StackOverflowServiceCoroutines) : ViewModel(), CoroutineScope {

    val liveDataDelegate = LiveDataDelegate("")

    var state by liveDataDelegate

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job

    fun load() {
        launch {
            try {
                val users = service.getTopUsers().await()
                val userStats: List<UserStats> =
                        users.take(5)
                                .map {
                                    async(coroutineContext) {
                                        userDetail(it)
                                    }
                                }
                                .map { it.await() }
                updateUi(userStats)
            } catch (e: Exception) {
                updateUi(e)
            }
        }
//        launch {
//            try {
//                val users = service.getTopUsers().await()
//                val userStats: List<UserStats> =
//                        users.take(5)
//                                .map { Triple(it, service.getTags(it.id), service.getBadges(it.id)) }
//                                .map { (user, tags, badges) -> UserStats(user, badges.await(), tags.await()) }
//                updateUi(userStats)
//            } catch (e: Exception) {
//                updateUi(e)
//            }
//        }
    }

    suspend fun userDetail(it: User): UserStats {
        val badges = service.getBadges(it.id)
        val tags = service.getTags(it.id)
        return UserStats(it, badges.await(), tags.await())
    }

    private suspend fun updateUi(s: Any) = withContext(Main) {
        state = s.toString()
    }


    override fun onCleared() {
        job.cancel()
    }
}