package it.codingjam.common

import io.reactivex.Single
import it.codingjam.common.model.Badge
import it.codingjam.common.model.Tag
import it.codingjam.common.model.User
import it.codingjam.common.utils.EnvelopePayload
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
