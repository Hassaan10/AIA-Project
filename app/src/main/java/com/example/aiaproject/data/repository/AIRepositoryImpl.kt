package com.example.aiaproject.data.repository

import com.example.aiaproject.data.model.Message
import com.example.aiaproject.data.model.Request
import com.example.aiaproject.data.network.ApiResponse
import com.example.aiaproject.data.network.NetworkService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AIRepositoryImpl @Inject constructor(private val apiService: NetworkService):
    AIRepository {
    override suspend fun ask(request: Request): Flow<ApiResponse<Message>> {
        return flow {
            emit(ApiResponse.Loading)
            try {
                val result = apiService.ask(request)
                emit(ApiResponse.Success(result.choices?.firstOrNull()?.message))
            }
            catch (e: Exception) {
                emit(ApiResponse.Error(e))
            }
        }
    }
}