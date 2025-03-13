package com.codewithdipesh.mangareader.domain.repository

import com.codewithdipesh.mangareader.data.local.entity.FavouriteManga
import com.codewithdipesh.mangareader.data.local.entity.VisitedChapter
import com.codewithdipesh.mangareader.domain.model.Chapter
import com.codewithdipesh.mangareader.domain.model.ChapterDetails
import com.codewithdipesh.mangareader.domain.model.DownloadedChapter
import com.codewithdipesh.mangareader.domain.model.Downloads
import com.codewithdipesh.mangareader.domain.model.Manga
import com.codewithdipesh.mangareader.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface MangaRepository {
   suspend fun getTopMangas() : Result<List<Manga>>
   suspend fun getAllMangas() : Result<List<Manga>>
   suspend fun searchManga(title : String) : Result<List<Manga>>
   fun getSearchHistory() : List<String>
   fun saveSearchHistory(searchTerm : String)
   suspend fun getAuthor(authorId:String) : Result<String>
   suspend fun getMangaById(mangaId:String) : Result<Manga>
   suspend fun getChapters(mangaId:String,limit:Int = 96,offset:Int =0,order:String = "asc") : Result<List<Chapter>>
   suspend fun getChapterPages(chapterId:String) : Result<ChapterDetails>
   suspend fun getChapterById(chapterId: String): Result<Chapter>
   suspend fun getAllVisitedChapters(): Result<List<VisitedChapter>>
   suspend fun addVisitedChapter(chapter: Chapter,coverImage :String)
   suspend fun getAllDownloads():Result<Downloads>
   suspend fun isDownloaded(chapteId: String):Boolean
   suspend fun getDownloadedChapterForManga(mangaId: String): Flow<List<DownloadedChapter>>
   suspend fun addFavouriteManga(manga: FavouriteManga)
   suspend fun deleteFavouriteManga(manga: FavouriteManga)
   suspend fun getAllFavouriteMangas(): Flow<List<FavouriteManga>>

}