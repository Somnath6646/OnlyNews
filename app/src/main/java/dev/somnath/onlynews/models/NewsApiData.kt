package dev.somnath.onlynews.models


import com.google.gson.annotations.SerializedName

data class NewsApiData(
    @SerializedName("articles")
    val articles: List<Article>,
    @SerializedName("status")
    val status: String,
    @SerializedName("totalResults")
    val totalResults: Int
)