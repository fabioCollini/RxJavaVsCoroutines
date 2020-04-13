package it.codingjam.common.utils

import io.reactivex.Flowable
import org.reactivestreams.Publisher
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class ExponentialBackoff(private val maxRetries: Int) :
        Function1<Flowable<out Throwable>, Publisher<*>> {

    private var retryCount = 0
    private var currentDelay = 100L + Random.nextInt(100)

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