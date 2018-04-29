package it.codingjam.coroutines

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import it.codingjam.common.ServiceFactory
import it.codingjam.common.arch.viewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val viewModel: ViewModel1 by viewModel(this) {
        ViewModel1(ServiceFactory.createRxJavaService(CoroutineCallAdapterFactory())).also { it.load() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.liveDataDelegate.observe(this) {
            text.text = it
        }
    }
}
