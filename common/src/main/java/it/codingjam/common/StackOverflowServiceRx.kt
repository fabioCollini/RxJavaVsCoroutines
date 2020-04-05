package it.codingjam.common

import io.reactivex.Single
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
