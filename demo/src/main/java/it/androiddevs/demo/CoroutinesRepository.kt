package it.androiddevs.demo

import it.codingjam.common.StackOverflowServiceCoroutines

class CoroutinesRepository(
        private val service: StackOverflowServiceCoroutines
) {
    suspend fun loadData() = service.getTopUsers()
}