package com.chrismagaa.cannabis.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import javax.inject.Inject

const val SETTINGS_PREFERENCES_KEY = "settings_preferences_key"
const val SETTINGS_MODE_THEME_KEY = "settings_theme_key"
const val SETTINGS_RSS_FEEDS_KEY = "settings_rss_feed_key"

val Context.datasotre: DataStore<Preferences> by preferencesDataStore(SETTINGS_PREFERENCES_KEY)

class PreferencesRepository @Inject constructor(@ApplicationContext private val context: Context) {
    companion object {
        val modeThemeKey = intPreferencesKey(SETTINGS_MODE_THEME_KEY)
        val rssFeedsKey = stringPreferencesKey(SETTINGS_RSS_FEEDS_KEY)
    }

    suspend fun saveModeTheme(theme: Int) {
        context.datasotre.edit {
            it[modeThemeKey] = theme
        }
    }

    fun getModeTheme() = context.datasotre.data.map { it[modeThemeKey]?: 0 }

    suspend fun saveRssFeeds(rssFeeds: String) {
        context.datasotre.edit {
            it[rssFeedsKey] = rssFeeds
        }
    }

    fun getRssFeeds() = context.datasotre.data.map { it[rssFeedsKey]?: ""}

}


