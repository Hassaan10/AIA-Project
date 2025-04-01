package com.example.aiaproject.data.network

import com.example.aiaproject.data.model.Request
import com.example.aiaproject.data.model.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface NetworkService {

    @POST("chat/completions")
    suspend fun ask(@Body request: Request): Response
}