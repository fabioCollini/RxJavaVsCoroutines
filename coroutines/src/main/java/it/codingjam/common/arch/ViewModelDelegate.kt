package it.codingjam.common.arch

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*

inline fun <reified VM : ViewModel> viewModel(activity: FragmentActivity, crossinline factory: () -> VM): Lazy<VM> {
    return lazy {
        ViewModelProviders.of(activity, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return factory() as T
            }
        }).get(VM::class.java)
    }
}

fun <T> LiveData<T>.observe(owner: LifecycleOwner, observer: (T?) -> Unit) =
        observe(owner, Observer { observer(it) })