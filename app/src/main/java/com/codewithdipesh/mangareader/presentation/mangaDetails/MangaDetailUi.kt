package com.codewithdipesh.mangareader.presentation.mangaDetails

import com.codewithdipesh.mangareader.domain.model.Chapter
import com.codewithdipesh.mangareader.domain.model.Rating
import com.codewithdipesh.mangareader.domain.model.Status
import com.codewithdipesh.mangareader.presentation.elements.MangaContent

data class MangaDetailUi(
    val id: String = "",
    val title: String = "",
    val altTitle: String? = null,
    val description: String? = null,
    val status: Status? = null,
    val year: Int? = null,
    val contentRating: Rating = Rating.Safe,
    val genres: List<String> = emptyList(),
    val themes: List<String> = emptyList(),
    val createdAt: String? = null,
    val isFavourite: Boolean = false,
    val totalChapter:Int = 0,

    val istotalChapterNull:Boolean = false,
    val chaptersAsc: List<Chapter> = emptyList(),
    val chaptersDesc: List<Chapter> = emptyList(),

    val authorId: String? = null,
    val author: String? = null,
    val coverImage: String? = null,

    val isLoading: Boolean = false,  // UI State
    val isChapterLoading: Boolean = false,
    val hasErrorOccured:Boolean = false,
    val isInternetAvailable : Boolean = true,
    val isChapterFetched : Boolean = false,
    val isAuthorFetched : Boolean = false,

    val selectedContent : MangaContent = MangaContent.Details,
    val currentPageAsc : Int = 0,
    val endReachedAsc :Boolean =false,
    val currentPageDesc : Int = 0,
    val endReachedDesc :Boolean =false,

    val selectedSortOrder :String = "desc",
    val hasToggledOnce :Boolean = false //for first time toggle i have to getChapters for other sorting order also
)
