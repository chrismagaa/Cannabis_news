package com.chrismagaa.cannabis.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chrismagaa.cannabis.repository.PreferencesRepository
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SettingsViewModel @Inject constructor(private val preferencesRepository: PreferencesRepository): ViewModel() {

    fun saveModeTheme(mode: Int){
        viewModelScope.launch(Dispatchers.IO) {
            preferencesRepository.saveModeTheme(mode)
        }
    }

    fun saveFeeds(hashMap: HashMap<String, Boolean>){
        viewModelScope.launch(Dispatchers.IO) {
            preferencesRepository.saveRssFeeds(Gson().toJson(hashMap))
        }
    }

}
