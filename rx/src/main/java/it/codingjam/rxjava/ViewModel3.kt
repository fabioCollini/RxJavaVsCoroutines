package it.codingjam.rxjava

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers.io
import it.codingjam.common.utils.ServiceFactory
import it.codingjam.common.StackOverflowServiceRx
import it.codingjam.common.model.User
import it.codingjam.common.model.UserStats

class ViewModel3 : ViewModel() {

    private val service: StackOverflowServiceRx = ServiceFactory.rx

    val state = MutableLiveData<String>()

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
                                { users: List<UserStats> ->
                                    updateUi(users)
                                },
                                { e -> updateUi(e) }
                        )
    }

    fun userDetail(user: User): Single<UserStats> {
        return service.getBadges(user.id)
                .map { badges ->
                    UserStats(user, badges)
                }
    }

    private fun updateUi(s: Any) {
        state.value = s.toString()
    }

    override fun onCleared() {
        disposable.clear()
    }
}