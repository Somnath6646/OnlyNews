package dev.somnath.onlynews.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import dev.somnath.onlynews.local.data.Bookmark
import dev.somnath.onlynews.models.Article
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkDao{

    @Insert
    suspend fun insertArticle(bookmarks: Bookmark): Long

    @Insert
    suspend fun insertArticle(bookmarks: List<Bookmark>)

    @Delete
    suspend fun deleteArticle(bookmarks: Bookmark)

    @Delete
    suspend fun deleteArticle(bookmarks: List<Bookmark>)

    @Query("SELECT * FROM ${Bookmark.TABLE_NAME} ORDER BY  bookmark_id DESC")
    fun getAllBookmark(): LiveData<List<Bookmark>>

    @Query("DELETE FROM ${Bookmark.TABLE_NAME}")
    suspend fun deleteAll()
}