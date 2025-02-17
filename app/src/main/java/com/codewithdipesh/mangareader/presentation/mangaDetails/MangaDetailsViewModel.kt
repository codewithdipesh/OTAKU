package com.codewithdipesh.mangareader.presentation.mangaDetails

import android.util.Log
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

    suspend fun load(mangaId: String, coverImage: String, title: String,authorId:String) {
        // Set basic details instantly (for faster UI update)
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getMangaById(mangaId)
            when (result) {
                is Result.Success -> {
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
                        altTitle = manga.altTitle
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

    private suspend fun getChapters(id : String){
        if(id == "") return
        val result = repository.getChapters(id)
        when(result){
            is Result.Success -> {
                val chapterList = result.data
                Log.e("MangaViewModel", "chapters: ${chapterList}")
                _state.value = _state.value.copy(
                    chapters = chapterList
                )
            }
            is Result.Error ->{
                Log.d("MangaViewmodel", "get chapters: Error ${result.error}")
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
}