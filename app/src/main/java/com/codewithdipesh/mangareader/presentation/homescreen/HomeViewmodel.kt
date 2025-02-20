package com.codewithdipesh.mangareader.presentation.homescreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codewithdipesh.mangareader.domain.repository.MangaRepository
import com.codewithdipesh.mangareader.domain.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
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
            launch(Dispatchers.IO) { getTopManga() }
            launch(Dispatchers.IO) { getAllManga() }
            launch(Dispatchers.IO) { loadhistory() }

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

    fun onChangeSearchValue(value : String){
        _state.value =_state.value.copy(
            searchValue = value
        )
    }

    fun clearSearchValue(){
        _state.value = _state.value.copy(
            searchValue = "",
        )
    }
    fun clearResultValue(){
        _state.value = _state.value.copy(
            searchResult = emptyList()
        )
    }

    fun loadhistory(){
        val history = repository.getSearchHistory()
        _state.value = _state.value.copy(
            history = history
        )
    }

    fun addSearchHistory(value : String){
        repository.saveSearchHistory(value)
        loadhistory()
    }

    suspend fun searchManga(){
        if(_state.value.searchValue == ""){
            Log.d("HomeViewmodel", "searchManga: Error")
        }else{
            _state.value = _state.value.copy(
                isloading = true
            )
            val result = repository.searchManga(_state.value.searchValue)
            addSearchHistory(_state.value.searchValue)
            when(result){
                is Result.Success -> {
                    _state.value = _state.value.copy(
                        searchResult = result.data,
                        isloading = false
                    )
                }
                is Result.Error ->{
                    _state.value = _state.value.copy(
                        isloading = false
                    )
                    Log.d("HomeViewmodel", "searchManga: Error ${result.error}")
                }

            }
        }
    }


}