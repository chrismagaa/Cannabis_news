package com.chrismagaa.cannabis.ui.feed

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.chrismagaa.cannabis.R
import com.chrismagaa.cannabis.databinding.ActivityMainBinding
import com.chrismagaa.cannabis.utils.ModeTheme
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val vmFeed: FeedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_SimpleLectorRSS)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        vmFeed.retrieveModeTheme()
        vmFeed.modeTheme.observeForever {
            if (it == ModeTheme.DARK.value) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }



        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //vmFeed.onCreate()

        setSupportActionBar(binding.toolbarMain)
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration =
            AppBarConfiguration(setOf(R.id.navigation_feed, R.id.navigation_saved))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }




}