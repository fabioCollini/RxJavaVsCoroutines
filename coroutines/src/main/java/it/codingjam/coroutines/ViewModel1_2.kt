package it.codingjam.coroutines

import androidx.lifecycle.MutableLiveData
import it.codingjam.common.StackOverflowServiceCoroutines
import it.codingjam.common.UserStats
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewModel1_2(private val service: StackOverflowServiceCoroutines) : ViewModel() {

    val state = MutableLiveData<String>()

    fun load() {
        viewModelScope.launch {
            try {
                val usersWithBadges = withContext(IO) {
                    val users = service.getTopUsers()
                    val firstUser = users.first()
                    val badges = service.getBadges(firstUser.id)
                    UserStats(firstUser, badges)
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