package com.codewithdipesh.mangareader.domain.repository

import com.codewithdipesh.mangareader.domain.model.Manga
import com.codewithdipesh.mangareader.domain.util.Result

interface MangaRepository {
   suspend fun getTopMangas() : Result<List<Manga>>
   suspend fun getAllMangas() : Result<List<Manga>>
}