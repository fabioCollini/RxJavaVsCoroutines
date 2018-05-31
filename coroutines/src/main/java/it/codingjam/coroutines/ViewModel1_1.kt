package it.codingjam.coroutines

import android.arch.lifecycle.ViewModel
import it.codingjam.common.UserStats
import it.codingjam.common.arch.LiveDataDelegate
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext

class ViewModel1_1(private val service: StackOverflowServiceCoroutines) : ViewModel() {

  val liveDataDelegate = LiveDataDelegate("")

  var state by liveDataDelegate

  private val job = Job()

  fun load() {
launch(CommonPool + job) {
  try {
    updateUi("loading users")
    val users = service.getTopUsers().await()
    updateUi("loading badges")
    val firstUser = users.first()
    val badges = service.getBadges(firstUser.id).await()
    val user = UserStats(firstUser, badges)
    updateUi(user)
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