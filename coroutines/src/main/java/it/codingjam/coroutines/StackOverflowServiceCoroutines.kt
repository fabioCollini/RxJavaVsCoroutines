package it.codingjam.coroutines

import it.codingjam.common.Badge
import it.codingjam.common.EnvelopePayload
import it.codingjam.common.User
import kotlinx.coroutines.experimental.Deferred
import retrofit2.http.GET
import retrofit2.http.Path

interface StackOverflowServiceCoroutines {

    @EnvelopePayload("items")
    @GET("/users")
    fun getTopUsers(): Deferred<List<User>>

    @EnvelopePayload("items")
    @GET("/users/{userId}/badges")
    fun getBadges(@Path("userId") userId: Int): Deferred<List<Badge>>
}
