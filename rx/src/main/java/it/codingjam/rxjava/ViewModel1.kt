package it.codingjam.rxjava

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers.io

class ViewModel1(private val service: StackOverflowServiceRx) : ViewModel() {

    val state = MutableLiveData<String>()

    private val disposable = CompositeDisposable()

    fun load() {
        disposable += service.getTopUsers()
                .map { it.first() }
                .flatMap { firstUser ->
                    service.getBadges(firstUser.id)
                }
                .subscribeOn(io())
                .observeOn(mainThread())
                .subscribe(
                        { badges -> updateUi(badges) },
                        { e -> updateUi(e) }
                )
    }

    private fun updateUi(s: Any) {
        state.value = s.toString()
    }

    override fun onCleared() {
        disposable.clear()
    }
}