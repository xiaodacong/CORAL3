package com.war_against_covid_19.coral.httphelper

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class NetworkInterceptor:Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain?.request()!!.newBuilder().addHeader("Connection", "close")
            ?.build()

        val response = chain?.proceed(originalRequest)
        return  response

    }
}