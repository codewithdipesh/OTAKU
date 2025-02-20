package com.codewithdipesh.mangareader.presentation.mangaDetails

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codewithdipesh.mangareader.domain.model.Manga
import com.codewithdipesh.mangareader.domain.repository.MangaRepository
import com.codewithdipesh.mangareader.domain.util.Result
import com.codewithdipesh.mangareader.presentation.elements.MangaContent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.internal.wait
import java.net.URLDecoder
import javax.inject.Inject

@HiltViewModel
class MangaDetailsViewModel @Inject constructor(
    private val repository: MangaRepository
): ViewModel()
{
    private val _state = MutableStateFlow(MangaDetailUi())
    val state = _state.asStateFlow()

    private val PAGE_SIZE:Int = 96

    suspend fun load(mangaId: String, coverImage: String, title: String,authorId:String) {
        // Set basic details instantly (for faster UI update)
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = _state.value.copy(
                isLoading = true
            )
            val result = repository.getMangaById(mangaId)
            when (result) {
                is Result.Success -> {
                    Log.e("Chapter Size", "viewmodel -> ${result.data.chapters}")
                    val manga = result.data
                    _state.value = state.value.copy(
                        id = manga.id,
                        description = manga.description ?: "",
                        status = manga.status,
                        year = manga.year,
                        contentRating = manga.contentRating,
                        genres = manga.genres,
                        themes = manga.themes,
                        createdAt = manga.createdAt,
                        isFavourite = manga.isFavourite,
                        isLoading = false,
                        altTitle = manga.altTitle,
                        totalChapter = manga.chapters,
                        isChapterLoading = manga.chapters == 0
                    )
                }
                is Result.Error -> {
                    Log.e("MangaViewModel", "Error fetching manga: ${result.error}")
                    _state.value = _state.value.copy(isLoading = false)
                }
            }
        }
        viewModelScope.launch(Dispatchers.IO) { getChapters(mangaId) }
        viewModelScope.launch(Dispatchers.IO) { getAuthor(authorId) }

        _state.value = _state.value.copy(
            coverImage = coverImage,
            title = title,
            authorId = authorId
        )

    }


    private suspend fun getAuthor(id :String){
        if(id == "") return
        val result = repository.getAuthor(id)
        when(result){
            is Result.Success ->{
                val name = result.data
                _state.value = _state.value.copy(
                    author = name
                )
            }
            is Result.Error -> {
                Log.d("MangaViewmodel", "get Author: Error ${result.error}")
            }
        }
    }

     suspend fun getChapters(id : String){
        if(id == "") return
        OnChapterLoadingState()
        val result = repository.getChapters(mangaId = id, limit = PAGE_SIZE, offset = _state.value.currentPage *PAGE_SIZE)
        when(result){
            is Result.Success -> {
                val chapterList = result.data
                if(chapterList.isNotEmpty()) {
                    _state.value = _state.value.copy(//endReached means fully loaded current page
                        chapters = _state.value.chapters + chapterList, // Append new data
                        currentPage = _state.value.currentPage+1,
                        endReached = (_state.value.chapters.size + chapterList.size) < ((_state.value.currentPage +1) * PAGE_SIZE)
                    )
                    if(_state.value.istotalChapterNull) {//it means we got null as totalChapters from fetching mangaById
                        _state.value.totalChapter + chapterList.size
                    }
                }
                OffChapterLoadingState()
            }
            is Result.Error ->{
                Log.d("MangaViewmodel", "get chapters: Error ${result.error}")
                OffChapterLoadingState()
            }
        }

    }

    fun changeSelectedContent(newContent : MangaContent){
        _state.value = _state.value.copy(
            selectedContent = newContent
        )
    }

    fun clearUi(){
        _state.value = MangaDetailUi()
    }

    fun OnChapterLoadingState(){
        _state.value = _state.value.copy(
            isChapterLoading = true
        )
    }
    fun OffChapterLoadingState(){
        _state.value = _state.value.copy(
            isChapterLoading = false
        )
    }
}