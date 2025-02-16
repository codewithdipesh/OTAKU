package com.codewithdipesh.mangareader.domain.repository

import com.codewithdipesh.mangareader.domain.model.Chapter
import com.codewithdipesh.mangareader.domain.model.Manga
import com.codewithdipesh.mangareader.domain.util.Result

interface MangaRepository {
   suspend fun getTopMangas() : Result<List<Manga>>
   suspend fun getAllMangas() : Result<List<Manga>>
   suspend fun searchManga(title : String) : Result<List<Manga>>
   fun getSearchHistory() : List<String>
   fun saveSearchHistory(searchTerm : String)
   suspend fun getAuthor(authorId:String) : Result<String>
   suspend fun getMangaById(mangaId:String) : Result<Manga>
   suspend fun getChapters(mangaId:String) : Result<List<Chapter>>
}