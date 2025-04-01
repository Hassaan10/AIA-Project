package com.example.aiaproject.data.network

sealed class ApiResponse<out T> {
    data object Empty : ApiResponse<Nothing>()
    data object Loading: ApiResponse<Nothing>()
    data class Success<out T>(val data: T?) : ApiResponse<T>()
    data class Error(val error: Exception) : ApiResponse<Nothing>()
}