package com.codewithdipesh.mangareader.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.codewithdipesh.mangareader.data.local.entity.DownloadedChapterEntity
import com.codewithdipesh.mangareader.data.local.entity.GenreEntity
import com.codewithdipesh.mangareader.data.local.entity.MangaEntity
import com.codewithdipesh.mangareader.data.local.entity.ThemeEntity
import com.codewithdipesh.mangareader.data.local.entity.VisitedChapter

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

    @Query("SELECT * FROM manga WHERE topManga = 1")
    suspend fun getCachedTopMangas(): List<MangaEntity>

    @Query("SELECT * FROM manga WHERE topManga = 0")
    suspend fun getCachedAllMangas(): List<MangaEntity>

    @Query("SELECT * FROM manga WHERE id = :mangaId")
    suspend fun getMangaById(mangaId: String): List<MangaEntity>

    @Query("SELECT * FROM visited_chapters WHERE mangaId = :mangaId")
    suspend fun getVisitedChapterForManga(mangaId: String): List<VisitedChapter>

    @Query("SELECT * FROM visited_chapters")
    suspend fun getAllVisitedChapters(): List<VisitedChapter>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addVisitedChapter(chapter : VisitedChapter)

    @Query("SELECT EXISTS(SELECT 1 FROM visited_chapters WHERE chapterid = :chapterId)")
    suspend fun getVisitedOrNot(chapterId : String):Boolean

    @Query("SELECT * FROM downloaded_chapter")
    suspend fun getAllDownloadedChapters() : List<DownloadedChapterEntity>

    @Query("SELECT * FROM downloaded_chapter WHERE mangaId = :mangaId")
    suspend fun getDownloadedChapterForManga(mangaId: String) : List<DownloadedChapterEntity>

    @Query("SELECT * FROM downloaded_chapter WHERE id = :chapterId")
    suspend fun getDownloadedChapter(chapterId: String) : DownloadedChapterEntity

}