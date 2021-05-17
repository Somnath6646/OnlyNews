package dev.somnath.onlynews.remote

import dev.somnath.onlynews.models.NewsApiData
import retrofit2.Response

import retrofit2.http.GET
import retrofit2.http.Query

interface NewsService {

    @GET("v2/top-headlines")
    suspend fun getTopHeadlinesByCategory(
        @Query("country") country: String,
        @Query("category") category: String,
        @Query("apiKey") apiKey: String
    ): Response<NewsApiData>


    @GET("v2/top-headlines")
    suspend fun getTopHeadlinesByCountry(
        @Query("country") country: String,
        @Query("apiKey") apiKey: String
    ): Response<NewsApiData>


    @GET("v2/everything")
    suspend fun getSearchResults(
        @Query("q") q: String,
        @Query("apiKey") apiKey: String
    ): Response<NewsApiData>
}