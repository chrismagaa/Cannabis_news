package com.chrismagaa.cannabis.ui.feed

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chrismagaa.cannabis.domain.model.Post
import com.chrismagaa.cannabis.domain.usecases.GetNewsUseCase
import com.chrismagaa.cannabis.domain.usecases.GetPostsSavedUseCase
import com.chrismagaa.cannabis.domain.usecases.UpdatePostUseCase
import com.chrismagaa.cannabis.repository.PreferencesRepository
import com.chrismagaa.cannabis.utils.RSSFeeds
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val getNewsUseCase: GetNewsUseCase,
    private val updatePostUseCase: UpdatePostUseCase,
    private val getPostsSavedUseCase: GetPostsSavedUseCase,
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    var modeTheme = MutableLiveData<Int>(0)
    val newPosts = MutableLiveData<List<Post>>()
    val savedPosts = MutableLiveData<List<Post>>()


    val loadingNewPosts = MutableLiveData<Boolean>()
    val loadingSavedPosts = MutableLiveData<Boolean>()

    private var listActivatedFeeds:List<String> =ArrayList<String>()

    init {
        getActivatedFeedRss()
    }

    fun getActivatedRssHashMap(): HashMap<String, Boolean> {
        val hashMapFeeds = HashMap<String, Boolean>()
        for (feed in RSSFeeds.values().toList()) {
           // hashMapFeeds[feed.title] = listActivatedFeeds.value!!.contains(feed.url)
            hashMapFeeds[feed.title] = listActivatedFeeds.contains(feed.url)
        }
        return hashMapFeeds
    }

    private fun getActivatedFeedRss() {
        viewModelScope.launch(Dispatchers.IO) {
            preferencesRepository.getRssFeeds().collect { it ->
                if (it.isNotEmpty()) {
                    val type = object : TypeToken<HashMap<String?, Boolean?>?>() {}.type
                    val mapFeeds = Gson().fromJson<HashMap<String?, Boolean?>>(it, type)

                    val arrayFeeds = ArrayList<String>()
                    for (feed in RSSFeeds.values().toList()) {
                        if (mapFeeds[feed.title] == true) {
                            arrayFeeds.add(feed.url)
                        }
                    }
                    //listActivatedFeeds.postValue(arrayFeeds)
                    listActivatedFeeds = arrayFeeds
                } else {
                    //listActivatedFeeds.postValue(RSSFeeds.values().map { rss -> rss.url })
                    listActivatedFeeds = RSSFeeds.values().map { rss -> rss.url }
                }
                getNewPosts()
            }

        }
    }

    fun getNewPosts() {
        viewModelScope.launch(Dispatchers.IO) {
            loadingNewPosts.postValue(true)
            val result = getNewsUseCase(listActivatedFeeds)
            if (result.isNotEmpty()) {
                newPosts.postValue(result)
            }
            loadingNewPosts.postValue(false)
        }
    }

    fun getSavedPosts() {
        viewModelScope.launch(Dispatchers.IO) {
            loadingSavedPosts.postValue(true)
            val result = getPostsSavedUseCase()
            savedPosts.postValue(result)
            loadingSavedPosts.postValue(false)
        }
    }

    fun updatePost(post: Post) {
        viewModelScope.launch {
            post.isFavorite = !post.isFavorite
            updatePostUseCase(post)
        }.invokeOnCompletion { getSavedPosts() }
    }

    fun retrieveModeTheme() {
        viewModelScope.launch(Dispatchers.IO) {
            preferencesRepository.getModeTheme().collect {
                modeTheme.postValue(it)
            }
        }
    }
}