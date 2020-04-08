package it.androiddevs.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.coroutineScope
import kotlinx.android.synthetic.main.activity_countdown.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CountDownActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_countdown)

        lifecycle.coroutineScope.launch {
            text_view.text = "3"
            delay(1000)
            text_view.text = "2"
            delay(1000)
            text_view.text = "1"
            delay(1000)
            text_view.text = "Go!"
        }
//        text_view.text = "3"
//        Thread.sleep(1000)
//        text_view.text = "2"
//        Thread.sleep(1000)
//        text_view.text = "1"
//        Thread.sleep(1000)
//        text_view.text = "Go!"
    }
}