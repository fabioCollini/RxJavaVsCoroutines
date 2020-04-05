package it.codingjam.rxjava

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers.io
import it.codingjam.common.utils.ServiceFactory
import it.codingjam.common.StackOverflowServiceRx
import java.util.concurrent.TimeUnit.SECONDS

class ViewModel5 : ViewModel() {

    private val service: StackOverflowServiceRx = ServiceFactory.rx

    val state = MutableLiveData<String>()

    private val disposable = CompositeDisposable()

    fun load() {
        disposable +=
                service.getTopUsers()
                        .subscribeOn(io())
                        .observeOn(mainThread())
                        .timeout(10, SECONDS)
                        .retry(3)
                        .subscribe(
                                { users -> updateUi(users) },
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