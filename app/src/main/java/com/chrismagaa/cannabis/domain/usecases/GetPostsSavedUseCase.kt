package com.chrismagaa.cannabis.domain.usecases

import com.chrismagaa.cannabis.domain.model.Post
import com.chrismagaa.cannabis.repository.FeedRepository
import javax.inject.Inject

class GetPostsSavedUseCase @Inject constructor(private val feedRepository: FeedRepository){
    suspend operator fun invoke(): List<Post> {
        return feedRepository.fetchPostsSaved()
    }
}