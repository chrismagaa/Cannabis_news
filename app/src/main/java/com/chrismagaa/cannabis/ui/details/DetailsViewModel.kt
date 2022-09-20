package com.chrismagaa.cannabis.ui.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chrismagaa.cannabis.domain.model.Post
import com.chrismagaa.cannabis.domain.usecases.UpdatePostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val updatePostUseCase: UpdatePostUseCase,
): ViewModel() {

    val isFavorite = MutableLiveData<Boolean>(false)


    fun setIsFavorite(isFavorite: Boolean) {
        this.isFavorite.postValue(isFavorite)
    }

    fun updatePost(post: Post) {
        post.isFavorite = !post.isFavorite
        viewModelScope.launch {
            updatePostUseCase(post)
        }
    }
}