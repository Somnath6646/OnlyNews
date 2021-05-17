package dev.somnath.onlynews.ui.viewmodels

import android.provider.ContactsContract
import android.util.Log
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import dev.somnath.onlynews.OnlyNewsApp
import dev.somnath.onlynews.local.data.Bookmark
import dev.somnath.onlynews.models.Article
import dev.somnath.onlynews.models.FragmentData
import dev.somnath.onlynews.repo.NewsRepository
import dev.somnath.onlynews.utils.Indicator
import dev.somnath.onlynews.utils.state.DataState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


private const val TAG = "ArticlesViewModel"

class ArticlesViewModel
@ViewModelInject constructor(
    private val repository: NewsRepository,
    @Assisted savedStateHandle: SavedStateHandle
) : ViewModel(), Observable{
    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    val apiKey = OnlyNewsApp.API_KEY


    @Bindable
    val query: MutableLiveData<String> = MutableLiveData()

    val feedArticlesDataState: MutableLiveData<DataState<List<Article>?>> = MutableLiveData()

    val searchedArticlesDataState: MutableLiveData<DataState<List<Article>?>> = MutableLiveData()

    val headlinesArticlesDataState: MutableLiveData<DataState<FragmentData>> = MutableLiveData()

    val category: MutableLiveData<String> = MutableLiveData("Technology")

    val navigate: MutableLiveData<Indicator<String>> = MutableLiveData()

    val bookmarks: LiveData<List<Bookmark>> = repository.bookmarks




    init {
        getFeedArticles()

    }



    fun getFeedArticles() {

        viewModelScope.launch {
            repository.getTopHeadlines("in", apiKey)
                .onEach {
                    feedArticlesDataState.value = it
                }.launchIn(viewModelScope)
        }

    }

    fun getHeadlinesArticles() {

        viewModelScope.launch {
            repository.getTopHeadlines("in", category.value!!, apiKey)
                .onEach {
                    headlinesArticlesDataState.value = it
                }.launchIn(viewModelScope)
        }

    }


    fun getSearchedArticles() {

        viewModelScope.launch {
            Log.i(TAG, "getSearchedArticles: ${query.value} ")
            repository.getSearchResults(query.value.toString(), apiKey)
                .onEach {
                    searchedArticlesDataState.value = it
                }.launchIn(viewModelScope)
        }

    }

    private val mutableSelectedArticle = MutableLiveData<Article>()
    val selectedArticle: LiveData<Article> get() = mutableSelectedArticle

    fun selectItem(article: Article) {
        mutableSelectedArticle.value = article
    }



    fun addABookmark(id: Int = 0, article: Article) {

        viewModelScope.launch {
            val bookMark = Bookmark(id, article)
            repository.insertArticleIntoBookmark(bookMark)
        }

    }

    fun addABookmark(id: Int = 0, articles: List<Article>) {

        viewModelScope.launch {
            for (article in articles) {
                val bookMark = Bookmark(id, article)
                repository.insertArticleIntoBookmark(bookMark)
            }
        }

    }

    fun addABookmark(bookmark: List<Bookmark>) {

        viewModelScope.launch {
            repository.insertArticleIntoBookmark(bookmark)
        }

    }

    fun deleteABookmark(bookmark: Bookmark) {
        viewModelScope.launch {
            repository.deleteArticlefromBookmark(bookmark)
        }
    }

    fun deleteABookmark(bookmark: List<Bookmark>) {
        viewModelScope.launch {
            repository.deleteArticlefromBookmark(bookmark)
        }
    }

    fun clearAllBookmark() {
        viewModelScope.launch {
            repository.deleteALlArticleFromBookmark()
        }
    }



}