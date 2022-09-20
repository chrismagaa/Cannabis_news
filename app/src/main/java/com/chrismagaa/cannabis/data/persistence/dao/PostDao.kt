package com.chrismagaa.cannabis.data.persistence.dao

import androidx.room.*
import com.chrismagaa.cannabis.data.persistence.entities.PostEntity

@Dao
interface PostDao {

    @Query("SELECT * FROM post_table WHERE isFavorite = 0 ORDER BY date DESC")
    suspend fun getAll(): List<PostEntity>

    @Query("SELECT * FROM post_table WHERE isFavorite = 1 ORDER BY date DESC")
    suspend fun getFavoritePosts(): List<PostEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
     suspend fun insertAll(posts: List<PostEntity>)

    @Query("DELETE FROM post_table WHERE isFavorite = 0")
     suspend fun deleteAll()

     @Update(onConflict = OnConflictStrategy.REPLACE)
     suspend fun updatePost(vararg post: PostEntity)

}