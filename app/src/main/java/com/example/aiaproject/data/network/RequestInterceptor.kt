package com.example.aiaproject.data.network

import okhttp3.Interceptor
import okhttp3.Response

class RequestHeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val modifiedRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer <YOUR API KEY>")
            .build()
        return chain.proceed(modifiedRequest)
    }

}