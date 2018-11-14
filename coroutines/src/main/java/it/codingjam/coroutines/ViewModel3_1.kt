package it.codingjam.coroutines

import it.codingjam.common.User
import it.codingjam.common.UserStats
import it.codingjam.common.arch.LiveDataDelegate
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class ViewModel3_1(private val service: StackOverflowServiceCoroutines) : ViewModel() {

    val liveDataDelegate = LiveDataDelegate("")

    var state by liveDataDelegate

    fun load() {
        viewModelScope.launch {
            try {
                val users = service.getTopUsers()
                val usersWithBadges: List<UserStats> =
                        users.take(5)
                                .map { user ->
                                    async {
                                        userDetail(user)
                                    }
                                }
                                .map { it.await() }
                updateUi(usersWithBadges)
            } catch (e: Exception) {
                updateUi(e)
            }
        }
    }

    suspend fun userDetail(user: User): UserStats = coroutineScope {
        val badges = async { service.getBadges(user.id) }
        UserStats(user, badges.await())
    }

    private fun updateUi(s: Any) {
        state = s.toString()
    }
}