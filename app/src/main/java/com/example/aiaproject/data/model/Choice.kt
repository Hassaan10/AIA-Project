package com.example.aiaproject.data.model


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class Choice(
    @SerializedName("finish_reason")
    val finishReason: String?,
    @SerializedName("index")
    val index: Int?,
    @SerializedName("message")
    val message: Message?,
    @SerializedName("native_finish_reason")
    val nativeFinishReason: String?
) : Parcelable