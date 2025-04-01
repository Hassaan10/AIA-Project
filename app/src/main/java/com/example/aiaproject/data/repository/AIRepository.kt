package com.example.aiaproject.data.repository

import com.example.aiaproject.data.model.Message
import com.example.aiaproject.data.model.Request
import com.example.aiaproject.data.network.ApiResponse
import kotlinx.coroutines.flow.Flow

interface AIRepository {

    suspend fun ask(request: Request): Flow<ApiResponse<Message>>
}