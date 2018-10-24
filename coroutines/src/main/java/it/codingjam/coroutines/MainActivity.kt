package it.codingjam.coroutines

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import it.codingjam.common.ServiceFactory
import it.codingjam.common.arch.viewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume

class MainActivity : AppCompatActivity(), CoroutineScope {

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Main + job

    private val viewModel by viewModel(this) {
        val service = ServiceFactory.createService<StackOverflowServiceCoroutines>(CoroutineCallAdapterFactory())
        ViewModel3(service).also { it.load() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.liveDataDelegate.observe(this) {
            text.text = it
        }

        text.setOnClickListener {
            launch {
                text.scale(2f)
                text.scale(1f)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    suspend fun View.scale(v: Float) =
            suspendCancellableCoroutine<Unit> { continuation ->
                val animator = animate()
                animator
                        .scaleX(v)
                        .scaleY(v)
                        .withEndAction { continuation.resume(Unit) }

                continuation.invokeOnCancellation {
                    animator.cancel()
                }
            }
}
