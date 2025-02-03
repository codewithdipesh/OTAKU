package com.codewithdipesh.mangareader.data.repository

import com.codewithdipesh.mangareader.data.repository.dto.MangaResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MangaApi {
    @GET("manga")
    suspend fun getTopManga(
        @Query("limit") limit: Int = 5,
    ):MangaResponse

    @GET("manga")
    suspend fun getAllManga(
        @Query("offset") offset: Int = 5,
        @Query("limit") limit: Int = 20
    ):MangaResponse

}