package it.codingjam.coroutines

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.codingjam.common.utils.ServiceFactory
import it.codingjam.common.StackOverflowServiceCoroutines
import it.codingjam.common.model.User
import it.codingjam.common.model.UserStats
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ViewModel3_1 : ViewModel() {

    private val service: StackOverflowServiceCoroutines = ServiceFactory.coroutines

    val state = MutableLiveData<String>()

    fun load() {
        viewModelScope.launch {
            try {
                val users = service.getTopUsers()
                val usersWithBadges: List<UserStats> =
                        users.take(5)
                                .map { user -> async { userDetail(user) } }
                                .map { it.await() }
                updateUi(usersWithBadges)
            } catch (e: Exception) {
                updateUi(e)
            }
        }
    }

    suspend fun userDetail(user: User) =
            UserStats(user, service.getBadges(user.id))

    private fun updateUi(s: Any) {
        state.value = s.toString()
    }
}