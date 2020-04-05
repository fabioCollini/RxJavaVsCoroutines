package it.codingjam.common

import it.codingjam.common.model.Badge
import it.codingjam.common.model.Tag
import it.codingjam.common.model.User
import it.codingjam.common.utils.EnvelopePayload
import retrofit2.http.GET
import retrofit2.http.Path

interface StackOverflowServiceCoroutines {

  @EnvelopePayload("items")
  @GET("/users")
  suspend fun getTopUsers(): List<User>

  @EnvelopePayload("items")
  @GET("/users/{userId}/badges")
  suspend fun getBadges(
      @Path("userId") userId: Int
  ): List<Badge>

  @EnvelopePayload("items")
  @GET("/users/{userId}/top-tags")
  suspend fun getTags(
      @Path("userId") userId: Int
  ): List<Tag>
}
