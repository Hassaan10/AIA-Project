package com.example.aiaproject.data.model


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class Request(
    @SerializedName("messages")
    val messages: List<Message>?,
    @SerializedName("model")
    val model: String? = "allenai/molmo-7b-d:free"
) : Parcelable