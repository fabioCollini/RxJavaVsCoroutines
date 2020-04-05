package it.codingjam.rxjava

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers.io
import it.codingjam.common.utils.ServiceFactory
import it.codingjam.common.StackOverflowServiceRx

class ViewModel1_1 : ViewModel() {

    private val service: StackOverflowServiceRx = ServiceFactory.rx

    val state = MutableLiveData<String>()
    private val disposable = CompositeDisposable()

    fun load() {
        updateUi("loading users")
        disposable += service.getTopUsers()
                .observeOn(mainThread())
                .doOnSuccess { updateUi("loading badges") }
                .observeOn(io())
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