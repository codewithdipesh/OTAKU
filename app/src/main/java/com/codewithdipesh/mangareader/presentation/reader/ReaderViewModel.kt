package com.codewithdipesh.mangareader.presentation.reader

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil3.ImageLoader
import coil3.request.ImageRequest
import com.codewithdipesh.mangareader.domain.repository.MangaRepository
import com.codewithdipesh.mangareader.domain.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReaderViewModel @Inject constructor(
    private val repository: MangaRepository,
) :ViewModel(){

    private val _state = MutableStateFlow(ReaderScreenUI())
    val uiState = _state.asStateFlow()

    private val HIGH_QUALITY_BASE_URL = "https://uploads.mangadex.org/data"
    private val LOW_QUALITY_BASE_URL = "https://uploads.mangadex.org/data-saver"

    private var preloadPages = mutableSetOf<Int>()

    fun load(chapterId: String,imageLoader: ImageLoader,context: Context){
        Log.d("load","loading->load")
        viewModelScope.launch {
            launch(Dispatchers.IO){ loadPages(chapterId,imageLoader,context) }
            launch(Dispatchers.IO){ getChapter(chapterId) }
        }
    }

    fun preloadPages(imageLoader: ImageLoader,context:Context){
        viewModelScope.launch(Dispatchers.Main){
            val preloadCount = 5
            val lastPreloadedPage = preloadPages.maxOrNull() ?:0
            for (i in 1..preloadCount) {
                val nextPage = lastPreloadedPage + i
                if (nextPage <= _state.value.pageSize && nextPage - 1 !in preloadPages) {
                    val pageLink = getPageLink(nextPage) ?: continue
                    val request = ImageRequest.Builder(context)
                        .data(pageLink)
                        .build()
                    imageLoader.enqueue(request) // Preload into cache
                    preloadPages.add(nextPage - 1) // Mark as preloaded (0-based index)
                    Log.d("Preload", "Preloading page $nextPage")
                }
            }
        }
    }

    fun increasePage(){
        if(_state.value.currentPage <_state.value.pageSize){
            _state.value = _state.value.copy(
                currentPage = _state.value.currentPage + 1
            )
        }
    }
    fun decreasePage(){
        if(_state.value.currentPage > 1){
            _state.value = _state.value.copy(
                currentPage = _state.value.currentPage - 1
            )
        }
    }

    fun getPageLink(pageNumber : Int): String{
        val hash = _state.value.hash
        if(hash != ""){
            if(_state.value.isHighQuality){
                return "$HIGH_QUALITY_BASE_URL/$hash/${_state.value.highQualityImageList[pageNumber-1]}"
            }else{
                return "$LOW_QUALITY_BASE_URL/$hash/${_state.value.lowQualityImageList[pageNumber-1]}"
            }
        }
        return ""

    }

    suspend fun loadPages(chapterId : String,imageLoader: ImageLoader,context: Context){
        startLoading()
        val result = repository.getChapterPages(chapterId)
        when(result){
            is Result.Success->{
                //todo update the list
                val chapterDetails = result.data
                _state.value = _state.value.copy(
                    highQualityImageList = chapterDetails.data,
                    lowQualityImageList = chapterDetails.dataSaver,
                    hash = chapterDetails.hash,
                )
                preloadPages(imageLoader,context)
                delay(10000)
                stopLoading()
                Log.d("load","loading->load->preload done")
            }
            is Result.Error -> {
                Log.d("chapterPages", result.error.message)
                stopLoading()
            }
        }
    }

    suspend fun getChapter(chapterId: String){
        startLoading()
        val result = repository.getChapterById(chapterId)
        when(result){
            is Result.Success ->{
                _state.value = _state.value.copy(
                    chapter = result.data,
                    currentPage = 1,
                    pageSize = result.data.pages
                )
                stopLoading()
            }
            is Result.Error -> {
                //todo error
                stopLoading()
            }
        }
    }

    fun startLoading(){
        _state.value = _state.value.copy(
            isLoading =  true,
            isPreloadComplete = false
        )
    }
    fun stopLoading(){
        _state.value = _state.value.copy(
            isLoading =  false,
            isPreloadComplete = true

        )
    }

    fun toggleHighQuality(){
        _state.value = _state.value.copy(
            isHighQuality = !_state.value.isHighQuality
        )
    }

    fun clearUi() {
        _state.value = ReaderScreenUI()
        preloadPages.clear()
    }
}