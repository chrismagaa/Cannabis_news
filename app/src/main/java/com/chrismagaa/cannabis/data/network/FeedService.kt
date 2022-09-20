package com.chrismagaa.cannabis.data.network

import com.chrismagaa.cannabis.RSSResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface FeedService {


    @GET
    suspend fun getRSSFeed(@Url url: String): Response<RSSResponse>


}