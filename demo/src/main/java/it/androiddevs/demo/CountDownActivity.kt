package it.androiddevs.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_countdown.*

class CountDownActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_countdown)

        text_view.text = "3"
        Thread.sleep(1000)
        text_view.text = "2"
        Thread.sleep(1000)
        text_view.text = "1"
        Thread.sleep(1000)
        text_view.text = "Go!"
    }
}