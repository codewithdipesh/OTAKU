package com.codewithdipesh.mangareader.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.codewithdipesh.mangareader.data.Preferences.DefaultPrefrences
import com.codewithdipesh.mangareader.data.local.MangaDatabase
import com.codewithdipesh.mangareader.data.local.dao.MangaDao
import com.codewithdipesh.mangareader.data.local.entity.MangaEntity
import com.codewithdipesh.mangareader.data.remote.MangaApi
import com.codewithdipesh.mangareader.data.repository.MangaRepositoryImpl
import com.codewithdipesh.mangareader.domain.repository.MangaRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MangaModule {

    @Provides
    @Singleton
    fun provideApiService(): MangaApi {
        return Retrofit.Builder()
            .baseUrl("https://api.mangadex.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MangaApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context : Context
    ): MangaDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = MangaDatabase::class.java,
            name = "manga_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideDao(
       database: MangaDatabase
    ): MangaDao {
        return database.mangaDao
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("search_prefs", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideDefaultPreferences(sharedPref : SharedPreferences): DefaultPrefrences {
        return DefaultPrefrences(sharedPref)
    }


    @Provides
    @Singleton
    fun provideRepository(
        api : MangaApi,
        dao : MangaDao,
        pref: DefaultPrefrences
    ): MangaRepository {
        return MangaRepositoryImpl(api,dao,pref)
    }


}