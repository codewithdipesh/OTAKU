package com.codewithdipesh.mangareader.presentation.downloads

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codewithdipesh.mangareader.data.repository.MangaRepositoryImpl
import com.codewithdipesh.mangareader.domain.model.Downloads
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

    fun getDownloadedManga(mangaId : String){
        val chapters = _state.value.downloads.downloads
            .filter { it.key.id == mangaId }
            .flatMap { it.value }

        _mangaState.value = _mangaState.value.copy(
            title = chapters[0].title ?: "",
            id = mangaId,
            chapters = chapters
        )
    }
}