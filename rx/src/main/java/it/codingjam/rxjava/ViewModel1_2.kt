package it.codingjam.rxjava

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers.io
import it.codingjam.common.utils.ServiceFactory
import it.codingjam.common.StackOverflowServiceRx
import it.codingjam.common.model.UserStats

class ViewModel1_2 : ViewModel() {

    private val service: StackOverflowServiceRx = ServiceFactory.rx

    val state = MutableLiveData<String>()

    private val disposable = CompositeDisposable()

    fun load() {
        disposable +=
                service.getTopUsers()
                        .map { it.first() }
                        .flatMap { firstUser ->
                            service.getBadges(firstUser.id)
                                    .map { badges ->
                                        UserStats(firstUser, badges)
                                    }
                        }
                        .subscribeOn(io())
                        .observeOn(mainThread())
                        .subscribe(
                                { user -> updateUi(user) },
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