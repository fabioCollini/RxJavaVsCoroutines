package it.codingjam.rxjava

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import it.codingjam.common.ServiceFactory
import it.codingjam.common.arch.observe
import it.codingjam.common.arch.viewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val viewModel by viewModel(this) {
        ViewModel0(ServiceFactory.createService(RxJava2CallAdapterFactory.create())).also { it.load() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.state.observe(this) {
            text.text = it
        }
    }
}