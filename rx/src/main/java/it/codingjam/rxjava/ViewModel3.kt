package it.codingjam.rxjava

import android.arch.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.Singles
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.Schedulers.io
import it.codingjam.common.UserStats
import it.codingjam.common.arch.LiveDataDelegate

class ViewModel3(private val service: StackOverflowServiceRx) : ViewModel() {

    val liveDataDelegate = LiveDataDelegate("")

    var state by liveDataDelegate

    private val disposable = CompositeDisposable()

    fun load() {
        disposable +=
                service.getTopUsers()
                        .map { it.first() }
                        .flatMap { firstUser ->
                            Singles.zip(
                                    service.getBadges(firstUser.id).subscribeOn(Schedulers.io()),
                                    service.getTags(firstUser.id).subscribeOn(Schedulers.io()),
                                    { badges, tags -> UserStats(firstUser, tags, badges) }
                            )
                        }
                        .subscribeOn(io())
                        .observeOn(mainThread())
                        .subscribe(
                                { pair: UserStats -> updateUi(pair) },
                                { e -> updateUi(e) }
                        )
    }

    private fun updateUi(s: Any) {
        state = s.toString()
    }

    override fun onCleared() {
        disposable.clear()
    }
}