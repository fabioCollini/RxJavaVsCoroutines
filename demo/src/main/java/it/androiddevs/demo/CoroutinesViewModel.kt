package it.androiddevs.demo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CoroutinesViewModel : ViewModel() {

    private val repository = CoroutinesRepository()

    val state = MutableLiveData<Any>()

    fun load() {
    }
}