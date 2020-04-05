package it.codingjam.coroutines

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.coroutineScope
import kotlinx.android.synthetic.main.activity_autocomplete.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class AutoCompleteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val items = mutableListOf<String>()
        val adapter = object : ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, items) {

            val noFilter = object : Filter() {

                override fun performFiltering(arg0: CharSequence?): Filter.FilterResults {
                    return FilterResults().apply {
                        values = items
                        count = items.size
                    }
                }

                override fun publishResults(arg0: CharSequence?, arg1: Filter.FilterResults?) {
                    notifyDataSetChanged()
                }
            }

            override fun getFilter() = noFilter
        }

        auto_complete.setAdapter(adapter)
        auto_complete.threshold = 1

        val channel: SendChannel<String> = lifecycle.coroutineScope.actor(capacity = UNLIMITED) {
            var lastJob: Job? = null
            consumeEach { s ->
                lastJob?.cancel()
                lastJob = launch {
                    delay(200)
                    val data = retrieveData(s)
                    adapter.clear()
                    adapter.addAll(data)
                }
            }
        }

        auto_complete.doOnTextChanged {
            channel.offer(it)
        }
    }

    fun TextView.doOnTextChanged(f: (String) -> Unit) {
        addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                f(s.toString())
            }
        })
    }

    suspend fun retrieveData(s: String): List<String> {
        println("retrieve $s")
        delay(200 + (5 - s.length).toLong() * 400)
        println("return $s")
        return ALL_STATES.filter { it.toLowerCase().contains(s.toLowerCase()) }
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

val ALL_STATES = listOf(
        "Alabama",
        "Alaska",
        "Arizona",
        "Arkansas",
        "California",
        "Colorado",
        "Connecticut",
        "Delaware",
        "Florida",
        "Georgia",
        "Hawaii",
        "Idaho",
        "Illinois",
        "Indiana",
        "Iowa",
        "Kansas",
        "Kentucky",
        "Louisiana",
        "Maine",
        "Maryland",
        "Massachusetts",
        "Michigan",
        "Minnesota",
        "Mississippi",
        "Missouri",
        "Montana",
        "Nebraska",
        "Nevada",
        "New Hampshire",
        "New Jersey",
        "New Mexico",
        "New York",
        "North Carolina",
        "North Dakota",
        "Ohio",
        "Oklahoma",
        "Oregon",
        "Pennsylvania",
        "Rhode Island",
        "South Carolina",
        "South Dakota",
        "Tennessee",
        "Texas",
        "Utah",
        "Vermont",
        "Virginia",
        "Washington",
        "West Virginia",
        "Wisconsin",
        "Wyoming"
)
