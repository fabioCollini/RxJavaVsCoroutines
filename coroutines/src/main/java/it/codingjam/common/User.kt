package it.codingjam.common

import com.google.gson.annotations.SerializedName

data class User(
        @SerializedName("user_id")
        val id: Int,
        val reputation: Int,
        @SerializedName("display_name")
        val name: String
)
