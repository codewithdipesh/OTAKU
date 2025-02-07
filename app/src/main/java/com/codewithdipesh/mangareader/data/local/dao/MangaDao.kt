package com.codewithdipesh.mangareader.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.codewithdipesh.mangareader.data.local.entity.GenreEntity
import com.codewithdipesh.mangareader.data.local.entity.MangaEntity
import com.codewithdipesh.mangareader.data.local.entity.ThemeEntity
import com.codewithdipesh.mangareader.domain.model.Manga

@Dao
interface MangaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMangas(manga: List<MangaEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGenre(genres: List<GenreEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTheme(themes: List<ThemeEntity>)

    @Query("SELECT name FROM genre WHERE mangaId = :mangaId")
    suspend fun getGenresForManga(mangaId: String): List<String>

    @Query("SELECT name FROM theme WHERE mangaId = :mangaId")
    suspend fun getThemesForManga(mangaId: String): List<String>

    @Query("SELECT * FROM manga")
    suspend fun getCachedMangas(): List<MangaEntity>


}