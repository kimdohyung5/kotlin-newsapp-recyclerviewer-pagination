package com.kimdo.knewsapp.di

import android.app.Application
import androidx.room.Room
import com.kimdo.knewsapp.api.NewsApi
import com.kimdo.knewsapp.db.ArticleDatabase
import com.kimdo.knewsapp.repository.NewsRepository
import com.kimdo.knewsapp.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideClient() : OkHttpClient {

        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    @Singleton
    @Provides
    fun provideApi(client: OkHttpClient): NewsApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(NewsApi::class.java)
    }


    @Singleton
    @Provides
    fun provideDatabase(application: Application) : ArticleDatabase {
        return Room.databaseBuilder(application, ArticleDatabase::class.java, "aaa.db").build()
    }


    @Singleton
    @Provides
    fun provideRepository(api: NewsApi, db: ArticleDatabase) : NewsRepository {
        return NewsRepository(api, db.dao)
    }

}