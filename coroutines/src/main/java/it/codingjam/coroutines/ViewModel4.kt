package it.codingjam.coroutines

import it.codingjam.common.User
import it.codingjam.common.UserStats
import it.codingjam.common.arch.LiveDataDelegate
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class ViewModel4(private val service: StackOverflowServiceCoroutines) : ViewModel() {

    val liveDataDelegate = LiveDataDelegate("")

    var state by liveDataDelegate

    fun load() {
        viewModelScope.launch {
            try {
                val users = service.getTopUsers()
                val userStats: List<UserStats> =
                        users.take(5)
                                .map {
                                    async {
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

    suspend fun userDetail(it: User): UserStats = coroutineScope {
        val badges = async { service.getBadges(it.id) }
        val tags = service.getTags(it.id)
        UserStats(it, badges.await(), tags)
    }

    private fun updateUi(s: Any) {
        state = s.toString()
    }
}