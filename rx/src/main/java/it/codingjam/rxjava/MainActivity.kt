package it.codingjam.rxjava

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.Filter
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers.io
import it.codingjam.common.ServiceFactory
import it.codingjam.common.arch.viewModel
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit.MILLISECONDS


class MainActivity : AppCompatActivity() {

    private val viewModel by viewModel(this) {
        ViewModel1_1(ServiceFactory.createService(RxJava2CallAdapterFactory.create())).also { it.load() }
    }

    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.liveDataDelegate.observe(this) {
            text.text = it
        }

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

        disposable = RxTextView.textChanges(auto_complete)
                .map { it.toString() }
                .debounce(300, MILLISECONDS)
                .filter { it.isNotEmpty() }
                .switchMapSingle { retrieveData(it).subscribeOn(io()) }
                .subscribeOn(io())
                .observeOn(mainThread())
                .subscribe {
                    adapter.clear()
                    adapter.addAll(it)
                }
    }

    fun retrieveData(s: String): Single<List<String>> {
        println("retrieve $s")
        return Single.just(ALL_STATES)
                .map { list -> list.filter { it.toLowerCase().contains(s.toLowerCase()) } }
                .delay(200 + (5 - s.length).toLong() * 400, MILLISECONDS)
                .doOnSuccess { println("End $s") }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
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
