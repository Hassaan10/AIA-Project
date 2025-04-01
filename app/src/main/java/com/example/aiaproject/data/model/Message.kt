package com.example.aiaproject.data.model


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class Message(
    @SerializedName("content")
    val content: String?,
    @SerializedName("role")
    val role: String?
) : Parcelable {
    fun isSentByUser (): Boolean {
        return role == "user"
    }
    fun isSentByAI (): Boolean {
        return role == "assistant"
    }
}