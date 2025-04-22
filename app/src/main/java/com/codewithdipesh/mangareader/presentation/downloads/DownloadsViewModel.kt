package com.codewithdipesh.mangareader.presentation.downloads

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.codewithdipesh.mangareader.data.repository.MangaRepositoryImpl
import com.codewithdipesh.mangareader.data.worker.DownloadChapterWorker
import com.codewithdipesh.mangareader.domain.model.Chapter
import com.codewithdipesh.mangareader.domain.model.DownloadedChapter
import com.codewithdipesh.mangareader.domain.model.Downloads
import com.codewithdipesh.mangareader.domain.model.ReadMode
import com.codewithdipesh.mangareader.domain.repository.MangaRepository
import com.codewithdipesh.mangareader.domain.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DownloadsViewModel @Inject constructor(
    private val repository: MangaRepository
) : ViewModel() {

    private val _state = MutableStateFlow(DownloadUiState())
    val state = _state.asStateFlow()

    private val _mangaState = MutableStateFlow(DownloadMangaUIState())
    val mangaState = _mangaState.asStateFlow()

    private val _chapterState = MutableStateFlow(DownloadedChapterUIState())
    val chapterState = _chapterState.asStateFlow()

    init {
        viewModelScope.launch (Dispatchers.IO){
            getDownloads()
        }
    }
    suspend fun getDownloads(){
        enableLoading()
        val response = repository.getAllDownloads()
        when(response){
            is Result.Success -> {
                Log.d("downloadViewModel", response.data.toString())
                _state.value = _state.value.copy(
                    downloads = response.data,
                    isLoading = false
                )
            }
            is Result.Error ->{
                //
                Log.d("DownloadViewModel",response.error.message)
            }
        }
    }
    fun enableLoading(){
        _state.value = _state.value.copy(
            isLoading = true
        )
    }

    //for showing chapter for a particular manga
    fun getDownloadedManga(mangaId: String, mangaName: String) {
        viewModelScope.launch {
            repository.getDownloadedChapterForManga(mangaId)
                .collect { chapters -> // Collect Flow
                    _mangaState.value = _mangaState.value.copy(
                        title = mangaName,
                        id = mangaId,
                        chapters = chapters // Now it's a List<DownloadedChapter>
                    )
                }
        }
    }

    //reader screen for downloaded chapter
    fun getDownloadedChapter(chapterId : String){
        _chapterState.value = _chapterState.value.copy(
            isLoading = true
        )
        val chapter = _mangaState.value.chapters.find { it.id == chapterId }
        if(chapter != null){
            Log.d("downloadViewModel","pages : ${chapter.content}")
            _chapterState.value = _chapterState.value.copy(
                chapterId = chapter.id,
                chapterName = chapter.title ?: "",
                chapterNumber = chapter.chapterNumber,
                pages = chapter.content,
                currentPage = 1,
                pageSize = chapter.pages,
                isLoading = false
            )
        }
        _chapterState.value = _chapterState.value.copy(
            isLoading = false
        )
    }
    fun clearChapterUi(){
        _chapterState.value = DownloadedChapterUIState()
    }
    fun increasePage(){
        _chapterState.value = _chapterState.value.copy(
            currentPage = _chapterState.value.currentPage + 1
        )
    }
    fun decreasePage(){
        _chapterState.value = _chapterState.value.copy(
            currentPage = _chapterState.value.currentPage - 1
        )
    }
    fun toggleReadMode(){
        _chapterState.value = _chapterState.value.copy(
            readMode =
            if(_chapterState.value.readMode == ReadMode.Vertical){
                ReadMode.Horizontal
            }else{
                ReadMode.Vertical
            },
            currentPage = 1
        )
    }

    fun toggleSelectionForDelete(chapter: DownloadedChapter) {
        val updatedList = _mangaState.value.selectedChapterForDelete.toMutableList().apply {
            if (contains(chapter)) remove(chapter) else add(chapter)
        }

        _mangaState.value = _mangaState.value.copy(
            selectedChapterForDelete = updatedList,
            isDeleteFormat = updatedList.isNotEmpty()
        )
    }


    fun turnOnDeleteMode(){
        _mangaState.value = _mangaState.value.copy(
            isDeleteFormat = true
        )
    }
    fun turnOffDeleteMode(){
        _mangaState.value = _mangaState.value.copy(
            isDeleteFormat = false,
            selectedChapterForDelete = emptyList()
        )
    }

    suspend fun deleteChapters(){
        if(_mangaState.value.selectedChapterForDelete.isNotEmpty()){
            repository.deleteDownloadedChapters(_mangaState.value.selectedChapterForDelete.map { it.id })
        }
    }

    suspend fun deleteDownloadedManga(){
        _state.value = _state.value.copy(
            downloads = _state.value.downloads
        )
    }
}