package dev.somnath.onlynews.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.somnath.onlynews.local.ArticlesDatabase
import dev.somnath.onlynews.local.dao.BookmarkDao
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object DatabaseModule {

    @Provides
    @Singleton
    fun provideArticlesDatabase(@ApplicationContext context: Context): ArticlesDatabase {
        return Room.databaseBuilder(
            context,
            ArticlesDatabase::class.java,
            ArticlesDatabase.DATABASE_NAME
        )
            .build()
    }

    @Provides
    @Singleton
    fun provideArticlesDAO(articlesDatabase: ArticlesDatabase): BookmarkDao {
        return articlesDatabase.bookmarkDao()
    }

}