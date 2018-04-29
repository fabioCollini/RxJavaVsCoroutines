package it.codingjam.rxjava

import android.arch.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import it.codingjam.common.arch.LiveDataDelegate

class ViewModel1(private val service: StackOverflowServiceRx) : ViewModel() {

    val liveDataDelegate = LiveDataDelegate("")

    var state by liveDataDelegate

    private val disposable = CompositeDisposable()

    fun load() {
        disposable += service.getTopUsers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { users -> state = users.toString() },
                        { exception -> state = exception.toString() }
                )
    }

    override fun onCleared() {
        disposable.clear()
    }
}