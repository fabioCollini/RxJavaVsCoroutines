package it.androiddevs.demo

import it.codingjam.common.StackOverflowServiceCoroutines
import it.codingjam.common.utils.ServiceFactory

class CoroutinesRepository(
        private val service: StackOverflowServiceCoroutines = ServiceFactory.coroutines
) {
    suspend fun loadData() = service.getTopUsers()
}