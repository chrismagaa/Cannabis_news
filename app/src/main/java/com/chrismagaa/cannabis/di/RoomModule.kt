package com.chrismagaa.cannabis.di

import android.content.Context
import androidx.room.Room
import com.chrismagaa.cannabis.data.persistence.FeedDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    const val FEED_DATABASE_NAME = "feed_database"

    @Provides
    @Singleton
    fun provideRoom(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context,
            FeedDatabase::class.java,
            FEED_DATABASE_NAME
        ).build()

    @Provides
    @Singleton
    fun providePostDao(db: FeedDatabase) = db.postDao()


}