package it.androiddevs.demo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.codingjam.common.utils.ServiceFactory
import kotlinx.coroutines.launch

class CoroutinesViewModel : ViewModel() {

    private val repository = CoroutinesRepository(ServiceFactory.coroutines)

    val state = MutableLiveData<Any>()

    fun load() {
        viewModelScope.launch {
            try {
                state.value = repository.loadData()
            } catch (e: Exception) {
                state.value = e.message
            }
        }
    }
}