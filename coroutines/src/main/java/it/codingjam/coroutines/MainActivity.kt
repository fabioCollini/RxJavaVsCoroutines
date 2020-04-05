package it.codingjam.coroutines

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import it.codingjam.common.ServiceFactory
import it.codingjam.common.arch.viewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModel(this) {
        val service = ServiceFactory.createService<StackOverflowServiceCoroutines>()
        ViewModel0(service).also { it.load() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.state.observe(this) {
            text.text = it
        }
    }
}