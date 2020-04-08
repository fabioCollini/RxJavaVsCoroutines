package it.androiddevs.demo

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import kotlinx.android.synthetic.main.activity_main.*

class DemoActivity : AppCompatActivity() {

    private val rxViewModel: RxViewModel by viewModels()

    private val coroutinesViewModel: CoroutinesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rxViewModel.state.observe(this) {
            text1.text = it.toString()
        }
        coroutinesViewModel.state.observe(this) {
            text2.text = it.toString()
        }
        if (savedInstanceState == null) {
            rxViewModel.load()
            coroutinesViewModel.load()
        }
    }
}
