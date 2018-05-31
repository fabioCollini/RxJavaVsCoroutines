package it.codingjam.coroutines

import android.arch.lifecycle.ViewModel
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
    val usersWithBadges: List<UserStats> =
        users.take(5)
            .map { user ->
              async(coroutineContext) {
                val badges = service.getBadges(user.id)
                UserStats(user, badges.await())
              }
            }
            .map { it.await() }
    updateUi(usersWithBadges)
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