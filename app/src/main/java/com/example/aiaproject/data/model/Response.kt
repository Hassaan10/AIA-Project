package com.example.aiaproject.data.model


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class Response(
    @SerializedName("choices")
    val choices: List<Choice?>?,
    @SerializedName("created")
    val created: Int?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("model")
    val model: String?,
    @SerializedName("object")
    val objectX: String?,
    @SerializedName("provider")
    val provider: String?,
    @SerializedName("usage")
    val usage: Usage?
) : Parcelable