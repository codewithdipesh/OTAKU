package com.codewithdipesh.mangareader.data.remote

import com.codewithdipesh.mangareader.data.remote.dto.MangaResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MangaApi {
    @GET("manga")
    suspend fun getTopManga(
        @Query("limit") limit: Int = 5,
    ): Response<MangaResponse>

    @GET("manga")
    suspend fun getAllManga(
        @Query("offset") offset: Int = 5,
        @Query("limit") limit: Int = 20
    ): Response<MangaResponse>

}