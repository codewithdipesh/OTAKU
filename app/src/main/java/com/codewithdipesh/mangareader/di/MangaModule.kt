package com.codewithdipesh.mangareader.di

import com.codewithdipesh.mangareader.data.remote.MangaApi
import com.codewithdipesh.mangareader.data.repository.MangaRepositoryImpl
import com.codewithdipesh.mangareader.domain.repository.MangaRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
    fun provideRepository(api : MangaApi): MangaRepository {
        return MangaRepositoryImpl(api)
    }


}