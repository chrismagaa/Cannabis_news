package com.chrismagaa.cannabis.repository

import android.util.Log
import com.chrismagaa.cannabis.data.network.FeedClient
import com.chrismagaa.cannabis.data.network.FeedInterceptor
import com.chrismagaa.cannabis.data.persistence.dao.PostDao
import com.chrismagaa.cannabis.data.persistence.entities.PostEntity
import com.chrismagaa.cannabis.domain.model.Post
import com.chrismagaa.cannabis.domain.model.toDomain
import javax.inject.Inject


class FeedRepository @Inject
constructor(
    private val feedClient: FeedClient,
    private val feedDao: PostDao) {


    suspend fun fetchFeedFromRSS(urls: List<String>): List<Post>{
        val posts = mutableListOf<Post>()
        for(url in urls){
            try{
                val response = feedClient.getFeed(url)
                posts.addAll(response.map { it.toDomain() })
            }catch (e: FeedInterceptor.NoInternetException){
                Log.d(FeedRepository::class.java.simpleName, e.message, e)
            }
        }
        return posts.toList()
    }

    suspend fun fetchFeedFromLocalDB(): List<Post>{
        val response = feedDao.getAll()
        return response.map { it.toDomain() }
    }

    suspend fun fetchPostsSaved(): List<Post>{
        val response = feedDao.getFavoritePosts()
        return response.map { it.toDomain() }
    }

    suspend fun insertFeed(feed: List<PostEntity>){
        feedDao.insertAll(feed)
    }

    suspend fun updatePost(post: PostEntity){
        feedDao.updatePost(post)
    }

    suspend fun clearFeed(){
        feedDao.deleteAll()
    }


}