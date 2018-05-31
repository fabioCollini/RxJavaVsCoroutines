package it.codingjam.rxjava

import android.arch.lifecycle.ViewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.Singles
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers.io
import it.codingjam.common.User
import it.codingjam.common.UserStats
import it.codingjam.common.arch.LiveDataDelegate

class ViewModel4(private val service: StackOverflowServiceRx) : ViewModel() {

    val liveDataDelegate = LiveDataDelegate("")

    var state by liveDataDelegate

    private val disposable = CompositeDisposable()

    fun load() {
        disposable +=
                service.getTopUsers()
                        .flattenAsObservable { it.take(5) }
                        .concatMapEager { user ->
                            userDetail(user).toObservable()
                        }
                        .toList()
                        .subscribeOn(io())
                        .observeOn(mainThread())
                        .subscribe(
                                { users: List<UserStats> -> updateUi(users) },
                                { e -> updateUi(e) }
                        )
    }

private fun userDetail(user: User): Single<UserStats> {
  return Singles.zip(
      service.getBadges(user.id).subscribeOn(io()),
      service.getTags(user.id).subscribeOn(io()),
      { badges, tags -> UserStats(user, badges, tags) }
  )
}

  private fun updateUi(s: Any) {
        state = s.toString()
    }

    override fun onCleared() {
        disposable.clear()
    }
}