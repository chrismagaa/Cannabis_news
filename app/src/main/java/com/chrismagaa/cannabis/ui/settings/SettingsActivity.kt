package com.chrismagaa.cannabis.ui.settings

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import com.chrismagaa.cannabis.R
import com.chrismagaa.cannabis.databinding.ActivitySettingsBinding
import com.chrismagaa.cannabis.utils.ModeTheme
import com.google.android.material.chip.Chip
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity() {

    companion object{
        const val EXTRA_MODE_THEME = "extra_mode_theme"
        const val EXTRA_FEEDS_ACTIVATED = "extra_feeds_activated"
    }

    private val vmSettings: SettingsViewModel by viewModels()

    private lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setupToolbar()


        setupTheme()

        setFeedsChips()

        setContentView(binding.root)
    }

    private fun setupTheme() {
        val modeTheme = intent.getIntExtra(EXTRA_MODE_THEME, ModeTheme.LIGHT.value)
        binding.switchDarkTheme.isChecked = (modeTheme == ModeTheme.DARK.value)

        binding.switchDarkTheme.setOnClickListener {
                if(binding.switchDarkTheme.isChecked){
                    vmSettings.saveModeTheme(ModeTheme.DARK.value)
                }else{
                    vmSettings.saveModeTheme(ModeTheme.LIGHT.value)
                }
            }
    }

    private fun setupToolbar() {
            setSupportActionBar(binding.toolbarSettings)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            binding.toolbarSettings.setNavigationOnClickListener {
                onBackPressed()
            }
    }

    private fun setFeedsChips(){
        val feedsActivated = intent.getStringExtra(EXTRA_FEEDS_ACTIVATED)

        val type = object : TypeToken<HashMap<String?, Boolean?>?>() {}.type
        val mapFeeds = Gson().fromJson<HashMap<String?, Boolean?>>(feedsActivated, type)
        for((name, state) in mapFeeds){
            val feedChip: Chip = layoutInflater.inflate(R.layout.item_chip_feed, null, false) as Chip
            feedChip.text = name
            feedChip.isChecked = state?: true
            binding.chipGroupBlogs.addView(feedChip)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        saveActivatedRssFeeds()
        finish()
    }

    private fun saveActivatedRssFeeds() {
        val hasMapFeeds = HashMap<String, Boolean>()
        binding.chipGroupBlogs.children.forEach {
            val chip = it as Chip
            val name = chip.text.toString()
            val state = chip.isChecked
            hasMapFeeds[name] = state
        }
        vmSettings.saveFeeds(hasMapFeeds)

       //RSSFeedsUtils.saveHashMapFeeds(spSettings, hasMapFeeds)
    }
}