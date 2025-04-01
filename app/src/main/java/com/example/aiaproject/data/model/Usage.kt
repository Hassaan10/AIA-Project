package com.example.aiaproject.data.model


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class Usage(
    @SerializedName("completion_tokens")
    val completionTokens: Int?,
    @SerializedName("prompt_tokens")
    val promptTokens: Int?,
    @SerializedName("total_tokens")
    val totalTokens: Int?
) : Parcelable