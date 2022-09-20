package com.chrismagaa.cannabis.data.network

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

class FeedInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return if (!isInternetAvailable()) {
            throw NoInternetException()
        } else {
            val original = chain.request()
            chain.proceed(original)
        }

    }

    private fun isInternetAvailable(): Boolean {
        return try {
            val timeoutMs = 1500
            val sock = Socket()
            val sockaddr = InetSocketAddress("8.8.8.8", 53)

            sock.connect(sockaddr, timeoutMs)
            sock.close()

            true
        } catch (e: IOException) {
            false
        }
    }

    class NoInternetException() : IOException() {
        override val message: String
            get() =
                "No internet available, please check your connected WIFi or Data"
    }
}