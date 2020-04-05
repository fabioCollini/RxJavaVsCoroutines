package it.codingjam.common

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
