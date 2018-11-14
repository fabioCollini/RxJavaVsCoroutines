package it.codingjam.coroutines

import androidx.lifecycle.MutableLiveData
import it.codingjam.common.UserStats
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ViewModel2(private val service: StackOverflowServiceCoroutines) : ViewModel() {

    val state = MutableLiveData<String>()

    fun load() {
        viewModelScope.launch {
            try {
                val users = service.getTopUsers()
                val usersWithBadges: List<UserStats> =
                        users.take(5)
                                .map { it to async { service.getBadges(it.id) } }
                                .map { (user, badges) ->
                                    UserStats(user, badges.await())
                                }
                updateUi(usersWithBadges)
            } catch (e: Exception) {
                updateUi(e)
            }
        }
    }

    private fun updateUi(s: Any) {
        state.value = s.toString()
    }
}