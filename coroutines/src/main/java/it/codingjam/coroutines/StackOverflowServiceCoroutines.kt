package it.codingjam.coroutines

import it.codingjam.common.Badge
import it.codingjam.common.EnvelopePayload
import it.codingjam.common.Tag
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
    fun getBadges(
            @Path("userId") userId: Int
    ): Deferred<List<Badge>>

    @EnvelopePayload("items")
    @GET("/users/{userId}/top-tags")
    fun getTags(
            @Path("userId") userId: Int
    ): Deferred<List<Tag>>
}
