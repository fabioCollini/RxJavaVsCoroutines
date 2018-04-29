package it.codingjam.coroutines

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import it.codingjam.common.ServiceFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        async(UI) {
            val service = ServiceFactory.createRxJavaService<StackOverflowServiceCoroutines>(CoroutineCallAdapterFactory())
            try {
                text.text = service.getTopUsers().await().toString()
            } catch (e: Exception) {
                text.text = e.message
            }
        }
    }
}
