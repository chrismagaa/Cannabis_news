package com.chrismagaa.cannabis.utils

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

object AdmobManager {

    const val preferences = "AdmobManager"
    const val openAppCountPreferences = "openAppCountPreferences"
    var mInterstitialAd: InterstitialAd? = null
    const val idIntersticial = "ca-app-pub-4702215318330979/1666825642"
    private const val LIMIT_OPEN_APP = 3

    fun loadAd(activity: Activity, onShow: () -> Unit) {
        val openAppCount = getTimesSharedPreference(activity)

        if(openAppCount < LIMIT_OPEN_APP) {
            setTimesSharedPreference(activity, openAppCount + 1)
            onShow()
        }else{
            if (mInterstitialAd == null) {
                var adRequest = AdRequest.Builder().build()
                InterstitialAd.load(
                    activity, idIntersticial, adRequest,
                    object : InterstitialAdLoadCallback() {
                        override fun onAdFailedToLoad(adError: LoadAdError) {
                            Log.d(this@AdmobManager.javaClass.simpleName, adError.message)
                            mInterstitialAd = null
                            onShow()
                        }

                        override fun onAdLoaded(interstitialAd: InterstitialAd) {
                            Log.d(this@AdmobManager.javaClass.simpleName, "Ad was loaded.")
                            mInterstitialAd = interstitialAd
                            showAd(activity, onShow)
                        }
                    }
                )
            }else{
                Log.d(this@AdmobManager.javaClass.simpleName, "Ya estaba cargado")
            }
        }
    }


    fun showAd(activity: Activity, onShowAd: () -> Unit) {
        if (mInterstitialAd != null) {
            mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    mInterstitialAd = null
                    onShowAd()
                }

                override fun onAdFailedToShowFullScreenContent(var1: AdError) {
                    mInterstitialAd = null
                    onShowAd()
                }


                override fun onAdShowedFullScreenContent() {
                    mInterstitialAd = null
                }
            }
            mInterstitialAd?.show(activity)
        }else{
            onShowAd()
        }
    }


    fun getTimesSharedPreference(activity: Activity): Int {
        val sharedPref = activity.getSharedPreferences(preferences, Context.MODE_PRIVATE);
        return sharedPref.getInt(openAppCountPreferences, 0)
    }

    fun setTimesSharedPreference(activity: Activity, times: Int) {
        val sharedPref = activity.getSharedPreferences(preferences, Context.MODE_PRIVATE);
        with(sharedPref.edit()) {
            putInt(openAppCountPreferences, times)
            commit()
        }
    }


}