package com.codewithdipesh.mangareader.presentation.mangaDetails

import androidx.compose.runtime.mutableStateOf
import com.codewithdipesh.mangareader.domain.model.Chapter
import com.codewithdipesh.mangareader.domain.model.Manga
import com.codewithdipesh.mangareader.domain.model.Rating
import com.codewithdipesh.mangareader.domain.model.Status
import com.codewithdipesh.mangareader.presentation.elements.MangaContent

data class MangaDetailUi(
    val id: String = "",
    val title: String = "",
    val altTitle: String? = null,
    val description: String? = null,
    val status: Status = Status.Completed,
    val year: Int? = null,
    val contentRating: Rating = Rating.Safe,
    val genres: List<String> = emptyList(),
    val themes: List<String> = emptyList(),
    val createdAt: String? = null,
    val isFavourite: Boolean = false,
    val totalChapter:Int = 0,
    val istotalChapterNull:Boolean = false,
    val chapters: List<Chapter> = emptyList(),
    val authorId: String? = null,
    val author: String? = null,
    val coverImage: String? = null,
    val isLoading: Boolean = false,  // UI State
    val isChapterLoading: Boolean = false,
    val hasErrorOccured:Boolean = false,
    val isInternetAvailable : Boolean = true,

    val selectedContent : MangaContent = MangaContent.Details,
    val currentPage : Int = 0,
    val endReached :Boolean =false,
)
