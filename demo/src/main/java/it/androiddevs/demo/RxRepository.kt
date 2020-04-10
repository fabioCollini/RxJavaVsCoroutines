package it.androiddevs.demo

import it.codingjam.common.StackOverflowServiceRx
import it.codingjam.common.utils.ServiceFactory

class RxRepository(
        private val service: StackOverflowServiceRx = ServiceFactory.rx
) {
    fun loadData() = service.getTopUsers()
}