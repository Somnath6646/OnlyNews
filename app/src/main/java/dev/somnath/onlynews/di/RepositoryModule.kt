package dev.somnath.onlynews.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dev.somnath.onlynews.local.dao.BookmarkDao
import dev.somnath.onlynews.remote.NewsService
import dev.somnath.onlynews.repo.NewsRepository
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object RepositoryModule {

    @Singleton
    @Provides
    fun provideNewsRepository(newsService: NewsService, bookmarkDao: BookmarkDao): NewsRepository{
        return NewsRepository(newsService, bookmarkDao)
    }

}