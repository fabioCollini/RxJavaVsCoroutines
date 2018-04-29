package it.codingjam.common.arch

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.FragmentActivity

inline fun <reified VM : ViewModel> viewModel(activity: FragmentActivity, crossinline factory: () -> VM): Lazy<VM> {
    return lazy {
        ViewModelProviders.of(activity, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return factory() as T
            }
        }).get(VM::class.java)
    }
}