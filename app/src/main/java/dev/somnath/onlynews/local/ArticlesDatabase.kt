package dev.somnath.onlynews.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.somnath.onlynews.local.dao.BookmarkDao
import dev.somnath.onlynews.local.data.Bookmark

@Database(entities = [Bookmark::class], version = 1, exportSchema = false)
abstract class ArticlesDatabase : RoomDatabase() {

    abstract fun bookmarkDao(): BookmarkDao

    companion object {
      const  val DATABASE_NAME = "onlynews_database"
    }

}