package com.chrismagaa.cannabis.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.chrismagaa.cannabis.databinding.ActivitySplashBinding
import com.chrismagaa.cannabis.ui.feed.MainActivity
import com.chrismagaa.cannabis.utils.AdmobManager

class SplashActivity : AppCompatActivity() {



    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)


        AdmobManager.loadAd(this){
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }

        setContentView(binding.root)
    }
}