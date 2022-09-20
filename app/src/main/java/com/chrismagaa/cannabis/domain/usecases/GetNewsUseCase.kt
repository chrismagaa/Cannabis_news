package com.chrismagaa.cannabis.domain.usecases

import com.chrismagaa.cannabis.repository.FeedRepository
import com.chrismagaa.cannabis.data.persistence.entities.toLocalDB
import com.chrismagaa.cannabis.domain.model.Post
import javax.inject.Inject

class GetNewsUseCase @Inject constructor(private val feedRepository: FeedRepository) {

    suspend operator fun invoke(urls: List<String>): List<Post> {
        val posts = feedRepository.fetchFeedFromRSS(urls)

         if(posts.isNotEmpty()) {
             feedRepository.clearFeed()
             feedRepository.insertFeed(posts.map { it.toLocalDB() })
         }

        return feedRepository.fetchFeedFromLocalDB()
    }
}