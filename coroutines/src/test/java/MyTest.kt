
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import it.codingjam.common.ServiceFactory
import it.codingjam.coroutines.StackOverflowServiceCoroutines
import kotlinx.coroutines.experimental.*
import org.junit.Test
import java.util.concurrent.TimeUnit

class MyTest {
    @Test fun dispose() {
        runBlocking {
            val job = launch {
                println("start")
                val job2 = launch(coroutineContext) {
                    println("start2")
                    delay(1500, TimeUnit.MILLISECONDS)
                    println("end2")
                }
                launch(coroutineContext) {
                    println("start1")
                    delay(500, TimeUnit.MILLISECONDS)
                    println("end1")
                }.join()
                job2.join()
                println("endAll")
            }
            delay(100, TimeUnit.MILLISECONDS)
            job.cancelAndJoin()
            delay(2000, TimeUnit.MILLISECONDS)
        }
    }

    @Test fun disposeAsync() {
        runBlocking {
            val job = async {
                println("start")
                val job2 = async {
                    println("start2")
                    delay(1500, TimeUnit.MILLISECONDS)
                    println("end2")
                }
                async {
                    println("start1")
                    delay(500, TimeUnit.MILLISECONDS)
                    println("end1")
                }.join()
                job2.join()
                println("endAll")
            }
            delay(100, TimeUnit.MILLISECONDS)
            job.cancelAndJoin()
            delay(2000, TimeUnit.MILLISECONDS)
        }
    }

    @Test fun disposeWithContext() {
        runBlocking {
            val job = async {
                println("start")
                val job2 = async {
                    println("start2")
                    delay(1500, TimeUnit.MILLISECONDS)
                    println("end2")
                }
                withContext(CommonPool) {
                    println("start1")
                    delay(500, TimeUnit.MILLISECONDS)
                    println("end1")
                }
                job2.join()
                println("endAll")
            }
            delay(100, TimeUnit.MILLISECONDS)
            job.cancelAndJoin()
            delay(2000, TimeUnit.MILLISECONDS)
        }
    }

    @Test fun disposeComp() {
        runBlocking {
            val job = async {
                println("start")
                val j = async(coroutineContext) {
                    println("start1")
                    delay(500, TimeUnit.MILLISECONDS)
                    println("end1")
                }
                suspendCancellableCoroutine<Unit> { continuation ->
                    println("start2")
                    Thread.sleep(1500)
                    if (!continuation.isCancelled) {
                        println("end2")
                        continuation.resume(Unit)
                    }
                }
                j.join()
                println("endAll")
            }
            delay(100, TimeUnit.MILLISECONDS)
            println("cancel")
            job.cancelAndJoin()
            delay(2000, TimeUnit.MILLISECONDS)
            println("end")
        }
    }

    @Test fun disposeDeferred() {
        runBlocking {
            val job = async {
                println("start")
                withContext(CommonPool) {

                }
                val j = async(coroutineContext) {
                    println("start1")
                    delay(500, TimeUnit.MILLISECONDS)
                    println("end1")
                }
                createDeferred().await()
                j.join()
                println("endAll")
            }
            delay(100, TimeUnit.MILLISECONDS)
            println("cancel")
            job.cancelAndJoin()
            delay(2000, TimeUnit.MILLISECONDS)
            println("end")
        }
    }

    private fun createDeferred(): CompletableDeferred<Unit> {
        val deferred = CompletableDeferred<Unit>()
        println("start2")
        Thread {
            Thread.sleep(1500)
            if (!deferred.isCancelled) {
                println("end2")
                deferred.complete(Unit)
            }
        }
        return deferred
    }

    @Test fun disposeAsync2() {
        runBlocking {
            val request = launch {
                // it spawns two other jobs, one with its separate context
                val job1 = launch {
                    println("job1: I have my own context and execute independently!")
                    delay(1000)
                    println("job1: I am not affected by cancellation of the request")
                }
                // and the other inherits the parent context
                val job2 = launch(coroutineContext) {
                    delay(100)
                    println("job2: I am a child of the request coroutine")
                    delay(1000)
                    println("job2: I will not execute this line if my parent request is cancelled")
                }
                // request completes when both its sub-jobs complete:
                job1.join()
                job2.join()
            }
            delay(500)
            request.cancel() // cancel processing of the request
            delay(1000) // delay a second to see what happens
            println("main: Who has survived request cancellation?")
        }
    }

    @Test fun retrofit() = runBlocking {
        val job = launch {
            val service = ServiceFactory.createService<StackOverflowServiceCoroutines>(CoroutineCallAdapterFactory())

            service.getTopUsers()
                    .await()
                    .map {
                        println("Call ${it.id}")
                        service.getBadges(it.id).await()
                    }
        }
        delay(2, TimeUnit.SECONDS)
        println("Cancel")
        job.cancelAndJoin()
        delay(5, TimeUnit.SECONDS)
        println("End")
    }

    @Test fun retrofit2() = runBlocking {
        val job = launch {
            val service = ServiceFactory.createService<StackOverflowServiceCoroutines>(CoroutineCallAdapterFactory())

            service.getTopUsers()
                    .await()
                    .map {
                        println("Call ${it.id}")
                        launch {
                            service.getBadges(it.id).await()
                        }.join()
                    }
        }
        delay(2, TimeUnit.SECONDS)
        println("Cancel")
        job.cancelAndJoin()
        delay(5, TimeUnit.SECONDS)
        println("End")
    }
}