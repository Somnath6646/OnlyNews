package dev.somnath.onlynews.models


import androidx.room.Embedded
import androidx.room.Entity
import com.google.gson.annotations.SerializedName

data class Article(
    val author: String?="",
    var content: String?="",
    val description: String?="",
    val publishedAt: String?="",
    @Embedded val source: Source,
    var title: String?="",
    val url: String?="",
    val urlToImage: String?=""
)