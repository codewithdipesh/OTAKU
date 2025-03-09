package com.codewithdipesh.mangareader.presentation.navigation

import android.net.Uri
import com.codewithdipesh.mangareader.domain.model.Chapter
import com.codewithdipesh.mangareader.domain.model.DownloadedChapter
import com.codewithdipesh.mangareader.domain.model.Manga
import com.codewithdipesh.mangareader.domain.model.MangaDownloadedDetails
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URLEncoder

sealed class Screen(val route : String){
    object Home : Screen("home")
    object Search : Screen("search")
    object Favourites : Screen("favourites")
    object Downloads : Screen("downloads")
    object DownloadedManga : Screen("downloadedManga/{mangaId}/{mangaName}"){
        fun createRoute(manga : MangaDownloadedDetails) : String {
            return "downloadedManga/${manga.id}/${manga.title}"
        }
    }
    object DownloadedReader : Screen("downloadedReader/{chapterId}"){
        fun createRoute(chapter : DownloadedChapter) : String {
            return "downloadedReader/${chapter.id}"
        }
    }
    object Detail : Screen("detail/{mangaId}/{coverImage}/{title}/{authorId}"){
        fun createRoute(manga : Manga) : String {
            return "detail/${manga.id}/${Uri.encode(manga.coverImage)}/${Uri.encode(manga.title)}/${Uri.encode(manga.authorId)}"
        }
    }
    object Reader : Screen("reader/{chapterId}"){
        fun createRoute(chapter : Chapter) : String {
            return "reader/${chapter.id}"
        }
    }
}