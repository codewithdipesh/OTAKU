package com.codewithdipesh.mangareader.data.remote

import com.codewithdipesh.mangareader.data.remote.dto.AuthorResponse
import com.codewithdipesh.mangareader.data.remote.dto.Chapter
import com.codewithdipesh.mangareader.data.remote.dto.ChapterByIdResponse
import com.codewithdipesh.mangareader.data.remote.dto.ChapterPageResult
import com.codewithdipesh.mangareader.data.remote.dto.MangaByIdResponse
import com.codewithdipesh.mangareader.data.remote.dto.MangaResponse
import com.codewithdipesh.mangareader.data.remote.dto.chapterResult
import com.codewithdipesh.mangareader.data.remote.dto.coverImageResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
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

    @GET("manga")
    suspend fun searchManga(
        @Query("limit") limit: Int = 10,
        @Query("title") title: String
    ): Response<MangaResponse>

    @GET("/cover")
    suspend fun getCoverImage(
        @Query("manga[]") mangaId: String,
        @Query("limit") limit: Int = 1
    ):Response<coverImageResponse>

    @GET("/author/{id}")
    suspend fun getAuthor(
        @Path("id") authorId : String
    ): Response<AuthorResponse>

    @GET("/manga/{id}")
    suspend fun getMangaById(
        @Path("id") id : String
    ): Response<MangaByIdResponse>

    @GET("/chapter")
    suspend fun getChapters(
        @Query("manga") mangaId : String,
        @Query("order[volume]") volumeOrder : String = "asc",
        @Query("order[chapter]") chapterOrder : String ="asc",
        @Query("translatedLanguage[]") language : String="en",
        @Query("includeEmptyPages") emptyPages : Int= 0,
        @Query("limit") limit : Int=96,
        @Query("offset") offset : Int=0,
    ): Response<chapterResult>

    @GET("/chapter/{chapterId}")
    suspend fun getChapterById(
        @Path("chapterId") chapterId : String
    ):Response<ChapterByIdResponse>

    @GET("/at-home/server/{chapterId}")
    suspend fun getChapterPages(
        @Path("chapterId") chapterId : String
    ) : Response<ChapterPageResult>








}