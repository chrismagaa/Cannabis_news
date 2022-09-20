package com.chrismagaa.cannabis.di

import com.chrismagaa.cannabis.data.network.FeedInterceptor
import com.chrismagaa.cannabis.data.network.FeedService
import com.chrismagaa.cannabis.utils.XMLDateConverter
import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.*
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(FeedInterceptor())
            .build()
    }

    @Provides
    @Singleton
    fun provideTikXml(): TikXml {
        return TikXml.Builder()
            .addTypeConverter(Date::class.java, XMLDateConverter())
            .exceptionOnUnreadXml(false).build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, tikxml: TikXml): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://localhost/")
            .addConverterFactory( TikXmlConverterFactory.create(tikxml))
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideFeedRss(retrofit: Retrofit): FeedService {
        return retrofit.create(FeedService::class.java)
    }
}