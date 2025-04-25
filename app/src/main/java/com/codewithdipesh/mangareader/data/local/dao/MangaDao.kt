package com.codewithdipesh.mangareader.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.codewithdipesh.mangareader.data.local.entity.DownloadedChapterEntity
import com.codewithdipesh.mangareader.data.local.entity.FavouriteManga
import com.codewithdipesh.mangareader.data.local.entity.GenreEntity
import com.codewithdipesh.mangareader.data.local.entity.GenreIdName
import com.codewithdipesh.mangareader.data.local.entity.MangaEntity
import com.codewithdipesh.mangareader.data.local.entity.ThemeEntity
import com.codewithdipesh.mangareader.data.local.entity.ThemeIdName
import com.codewithdipesh.mangareader.data.local.entity.VisitedChapter
import kotlinx.coroutines.flow.Flow

@Dao
interface MangaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMangas(manga: List<MangaEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGenre(genres: List<GenreEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTheme(themes: List<ThemeEntity>)

    @Query("SELECT * FROM genre WHERE mangaId = :mangaId")
    suspend fun getGenresForManga(mangaId: String): List<GenreEntity>

    @Query("SELECT * FROM theme WHERE mangaId = :mangaId")
    suspend fun getThemesForManga(mangaId: String): List<GenreEntity>

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
    fun getDownloadedChapterForManga(mangaId: String) : Flow<List<DownloadedChapterEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addDownloadedChapter(chapter : DownloadedChapterEntity)

    @Query("UPDATE downloaded_chapter SET downloadStatus = :newStatus WHERE id = :chapterId")
    suspend fun updateDownloadedChapter(chapterId: String, newStatus: String)

    @Query("DELETE FROM downloaded_chapter WHERE id = :chapterId")
    suspend fun deleteDownloadedChapter(chapterId: String)

    @Query("SELECT * FROM downloaded_chapter WHERE id = :chapterId")
    suspend fun getDownloadedChapter(chapterId: String) : DownloadedChapterEntity

    @Query("SELECT COUNT(*) FROM downloaded_chapter")
    suspend fun getDownloadedChaptersCount(): Int

    @Query("SELECT EXISTS(SELECT 1 FROM downloaded_chapter WHERE id = :chapterId)")
    fun isDownloaded(chapterId: String) : Boolean

    @Insert(entity = FavouriteManga::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavouriteManga(manga : FavouriteManga)

    @Delete(entity = FavouriteManga::class)
    suspend fun deleteFavouriteManga(manga : FavouriteManga)

    @Query("SELECT * FROM favourite_manga")
    fun getFavouriteMangas() : Flow<List<FavouriteManga>>

    @Query("SELECT EXISTS(SELECT 1 FROM favourite_manga WHERE id = :mangaId)")
    fun isFavourite(mangaId: String) : Boolean

}