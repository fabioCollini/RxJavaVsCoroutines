package it.codingjam.common.model

data class UserStats(
        val user: User,
        val badges: List<Badge>,
        val tags: List<Tag> = emptyList()
)