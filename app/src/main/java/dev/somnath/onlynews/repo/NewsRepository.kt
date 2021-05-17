package dev.somnath.onlynews.repo

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dev.somnath.onlynews.local.dao.BookmarkDao
import dev.somnath.onlynews.local.data.Bookmark
import dev.somnath.onlynews.models.Article
import dev.somnath.onlynews.models.ErrorResponse
import dev.somnath.onlynews.models.FragmentData
import dev.somnath.onlynews.remote.NewsService
import dev.somnath.onlynews.utils.state.DataState
import kotlinx.coroutines.flow.*
import retrofit2.HttpException
import java.net.ConnectException
import java.net.UnknownHostException

private const val TAG = "NewsRepository"

class NewsRepository(
    val newsService: NewsService,
    val bookmarkDao: BookmarkDao
) : BaseRepository() {

    suspend fun getTopHeadlines(
        country: String,
        category: String,
        apiKey: String
    ): Flow<DataState<FragmentData>> = flow {
        emit(DataState.Loading)
        try {
            val response = newsService.getTopHeadlinesByCategory(country, category, apiKey)
            Log.i("Response aya", response.body().toString())


            if (response.isSuccessful) {

                val list = response.body()?.articles

                emit(DataState.Success(FragmentData(category, list)))



            } else {
                val gson = Gson()
                val type = object : TypeToken<ErrorResponse>() {}.type
                var errorResponse: ErrorResponse? =
                    gson.fromJson(response.errorBody()!!.charStream(), type)
                if (errorResponse != null) {
                    emit(DataState.Error(errorResponse.message))
                }

            }


        } catch (e: Exception) {

            if(e is ConnectException || e is HttpException || e is UnknownHostException){
                emit(DataState.Error("Connect to internet to see articles"))
            }else{
                emit(DataState.Error(e.message.toString()))
            }

        }

    }

    suspend fun getTopHeadlines(country: String, apiKey: String): Flow<DataState<List<Article>?>> =
        flow {
            emit(DataState.Loading)
            try {
                val response = newsService.getTopHeadlinesByCountry(country, apiKey)
                Log.i("Response aya", response.body().toString())


                if (response.isSuccessful) {

                    emit(DataState.Success(response.body()?.articles))


                } else {
                    val gson = Gson()
                    val type = object : TypeToken<ErrorResponse>() {}.type
                    var errorResponse: ErrorResponse? =
                        gson.fromJson(response.errorBody()!!.charStream(), type)
                    if (errorResponse != null) {
                        emit(DataState.Error(errorResponse.message))
                    }

                }


            } catch (e: Exception) {
                if (e is ConnectException || e is HttpException || e is UnknownHostException) {
                    emit(DataState.Error("Connect to internet to see articles"))
                } else {
                    emit(DataState.Error(" error "))
                }
            }

        }

    suspend fun getSearchResults(q: String, apiKey: String): Flow<DataState<List<Article>?>> =
        flow {
            emit(DataState.Loading)
            try {
                val response = newsService.getSearchResults(q, apiKey)
                Log.i("Response aya", response.body().toString())


                if (response.isSuccessful) {

                    emit(DataState.Success(response.body()?.articles))


                } else {
                    val gson = Gson()
                    val type = object : TypeToken<ErrorResponse>() {}.type
                    var errorResponse: ErrorResponse? =
                        gson.fromJson(response.errorBody()!!.charStream(), type)
                    if (errorResponse != null) {
                        emit(DataState.Error(errorResponse.message))
                    }

                }


            } catch (e: Exception) {
                if (e is ConnectException || e is HttpException || e is UnknownHostException) {
                    emit(DataState.Error("Connect to internet to see searched article"))
                } else {
                    emit(DataState.Error(" error "))
                }
            }

        }

    val bookmarks = bookmarkDao.getAllBookmark()

    suspend fun insertArticleIntoBookmark(bookmark: Bookmark){
        bookmarkDao.insertArticle(bookmark)
    }

    suspend fun insertArticleIntoBookmark(bookmark: List<Bookmark>){
        bookmarkDao.insertArticle(bookmark)
    }

    suspend fun deleteArticlefromBookmark(bookmark: Bookmark){
        bookmarkDao.deleteArticle(bookmark)
    }

    suspend fun deleteArticlefromBookmark(bookmark: List<Bookmark>){
        bookmarkDao.deleteArticle(bookmark)
    }

    suspend fun deleteALlArticleFromBookmark(){
        bookmarkDao.deleteAll()
    }



}