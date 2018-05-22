package it.codingjam.common

data class UserStats(
        val user: User,
        val tags: List<Tag>,
        val badges: List<Badge>
)