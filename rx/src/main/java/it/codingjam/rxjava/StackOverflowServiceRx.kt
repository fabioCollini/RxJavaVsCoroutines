package it.codingjam.rxjava

import io.reactivex.Single
import it.codingjam.common.Badge
import it.codingjam.common.EnvelopePayload
import it.codingjam.common.Tag
import it.codingjam.common.User
import retrofit2.http.GET
import retrofit2.http.Path

interface StackOverflowServiceRx {

    @EnvelopePayload("items")
    @GET("/users")
    fun getTopUsers(): Single<List<User>>

    @EnvelopePayload("items")
    @GET("/users/{userId}/badges")
    fun getBadges(
            @Path("userId") userId: Int
    ): Single<List<Badge>>

    @EnvelopePayload("items")
    @GET("/users/{userId}/top-tags")
    fun getTags(
            @Path("userId") userId: Int
    ): Single<List<Tag>>
}
