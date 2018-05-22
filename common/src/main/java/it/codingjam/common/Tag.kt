package it.codingjam.common

import com.google.gson.annotations.SerializedName

data class Tag(
        @SerializedName("tag_name")
        val name: String,
        @SerializedName("answer_count")
        val count: Int
) {
    override fun toString() = "$name $count"
}