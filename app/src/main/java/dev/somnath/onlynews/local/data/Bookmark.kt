package dev.somnath.onlynews.local.data

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.somnath.onlynews.models.Article

@Entity(tableName = Bookmark.TABLE_NAME)
data class Bookmark(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "bookmark_id")
    val id: Int,

    @Embedded val article: Article?
){

    companion object{
        const val TABLE_NAME = "Bookmark"
    }
}