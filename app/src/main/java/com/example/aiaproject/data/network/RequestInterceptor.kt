package com.example.aiaproject.data.network

import okhttp3.Interceptor
import okhttp3.Response

class RequestHeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val modifiedRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer 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")
            .build()
        return chain.proceed(modifiedRequest)
    }

}