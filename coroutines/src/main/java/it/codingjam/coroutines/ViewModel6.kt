package it.codingjam.coroutines

import android.arch.lifecycle.ViewModel
import it.codingjam.common.arch.LiveDataDelegate
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.UI
import java.util.*
import java.util.concurrent.TimeUnit.SECONDS

class ViewModel6(private val service: StackOverflowServiceCoroutines) : ViewModel() {

  val liveDataDelegate = LiveDataDelegate("")

  var state by liveDataDelegate

  private val job = Job()

  fun load() {
launch(CommonPool + job) {
  try {
    exponentialBackoff(3) {
      withTimeout(10, SECONDS) {
        val users = service.getTopUsers().await()
        updateUi(users)
      }
    }
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

suspend fun <T> exponentialBackoff(
    times: Int = 5,
    block: suspend () -> T): T {
  var currentDelay = 100 + Random().nextInt(100)
  repeat(times - 1) {
    try {
      return block()
    } catch (e: Exception) {
    }
    delay(currentDelay)
    currentDelay *= 2
  }
  return block()
}