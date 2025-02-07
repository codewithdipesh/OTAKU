package com.codewithdipesh.mangareader.presentation.homescreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codewithdipesh.mangareader.domain.repository.MangaRepository
import com.codewithdipesh.mangareader.domain.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewmodel @Inject constructor(
    private val repository : MangaRepository
):ViewModel(){

    private val _state = MutableStateFlow(HomeUiState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            getTopManga()
            getAllManga()
        }
    }

    suspend fun getTopManga(){
        val result = repository.getTopMangas()

        when(result){
            is Result.Success -> {
                _state.value = _state.value.copy(
                    topMangaList = result.data
                )
            }
            is Result.Error ->{
                Log.d("HomeViewmodel", "getTopManga: Error ${result.error}")
            }
        }
    }
    suspend fun getAllManga(){
        val result = repository.getAllMangas()
        Log.d("ViewModel ","getallManga: $result")

        when(result){
            is Result.Success -> {
                _state.value = _state.value.copy(
                    allMangas = result.data
                )
            }
            is Result.Error ->{
                Log.d("HomeViewmodel", "getTopManga: Error ${result.error}")
            }
        }
    }



}