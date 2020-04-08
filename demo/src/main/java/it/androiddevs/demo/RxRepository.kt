package it.androiddevs.demo

import it.codingjam.common.StackOverflowServiceRx

class RxRepository(
        private val service: StackOverflowServiceRx
) {
    fun loadData() = service.getTopUsers()
}