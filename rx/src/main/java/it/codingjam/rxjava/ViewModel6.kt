package it.codingjam.rxjava

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers.io
import it.codingjam.common.StackOverflowServiceRx
import org.reactivestreams.Publisher
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeUnit.SECONDS

class ViewModel6(private val service: StackOverflowServiceRx) : ViewModel() {

    val state = MutableLiveData<String>()

    private val disposable = CompositeDisposable()

    fun load() {
        disposable += service.getTopUsers()
                .subscribeOn(io())
                .observeOn(mainThread())
                .timeout(10, SECONDS)
                .retryWhen(ExponentialBackoff(3))
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

class ExponentialBackoff(private val maxRetries: Int) :
        Function1<Flowable<out Throwable>, Publisher<*>> {

    private var retryCount = 0
    private var currentDelay = 100L + Random().nextInt(100)

    override fun invoke(attempts: Flowable<out Throwable>): Publisher<*> {
        return attempts
                .flatMap { throwable ->
                    if (++retryCount < maxRetries)
                        Flowable.timer(currentDelay, TimeUnit.MILLISECONDS).also {
                            currentDelay *= 2
                        }
                    else
                        Flowable.error(throwable)
                }
    }
}