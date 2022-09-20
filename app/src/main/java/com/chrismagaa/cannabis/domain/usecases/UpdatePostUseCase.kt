package com.chrismagaa.cannabis.domain.usecases

import com.chrismagaa.cannabis.data.persistence.entities.toLocalDB
import com.chrismagaa.cannabis.domain.model.Post
import com.chrismagaa.cannabis.repository.FeedRepository
import javax.inject.Inject

class UpdatePostUseCase @Inject constructor(
    private val postRepository: FeedRepository
) {
    suspend operator fun invoke(post: Post){
        postRepository.updatePost(post.toLocalDB())
    }
}