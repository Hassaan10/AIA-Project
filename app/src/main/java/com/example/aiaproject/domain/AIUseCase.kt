package com.example.aiaproject.domain

import com.example.aiaproject.data.model.Message
import com.example.aiaproject.data.model.Request
import com.example.aiaproject.data.network.ApiResponse
import com.example.aiaproject.data.repository.AIRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AIUseCase @Inject constructor(private val repository: AIRepository) {

    private val messages = mutableListOf<Message>()

    suspend operator fun invoke(content: String): Flow<ApiResponse<List<Message>>> {
        val message = Message(role = "user", content = content)
        val request = Request(
            messages = listOf(message))

        messages.add(message)

        return flow {
            repository.ask(request).collect { response ->
                when (response) {
                    is ApiResponse.Success -> {
                        response.data?.let { messages.add(it) }
                        emit(ApiResponse.Success(messages.toList()))
                    }
                    is ApiResponse.Error -> emit(response)
                    is ApiResponse.Loading -> emit(response)
                    is ApiResponse.Empty -> {
                        // Handle empty state if needed
                    }
                }
            }
        }
    }
}