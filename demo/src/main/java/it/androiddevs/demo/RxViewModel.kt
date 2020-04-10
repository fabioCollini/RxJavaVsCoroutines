package it.androiddevs.demo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers.io
import it.codingjam.common.utils.ServiceFactory

class RxViewModel : ViewModel() {

    private val repository = RxRepository(ServiceFactory.rx)

    val state = MutableLiveData<Any>()

    private val disposable = CompositeDisposable()

    fun load() {
        disposable += repository.loadData()
                .subscribeOn(io())
                .observeOn(mainThread())
                .subscribe(
                        { users -> state.value = users },
                        { state.value = it.message }
                )
    }

    override fun onCleared() {
        disposable.clear()
    }
}