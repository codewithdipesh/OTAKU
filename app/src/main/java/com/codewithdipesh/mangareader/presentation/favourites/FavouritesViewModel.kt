package com.codewithdipesh.mangareader.presentation.favourites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codewithdipesh.mangareader.data.local.entity.FavouriteManga
import com.codewithdipesh.mangareader.domain.repository.MangaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouritesViewModel @Inject constructor(
     private val repository: MangaRepository
): ViewModel() {

    private val _state = MutableStateFlow<List<FavouriteManga>>(emptyList())
    val mangaList = _state.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO){
            getFavourites()
        }
    }

    suspend fun toggleFavourite (manga: FavouriteManga){
        _state.value -= manga
        repository.deleteFavouriteManga(manga)
    }
    suspend fun getFavourites(){
        repository.getAllFavouriteMangas().collect{
            _state.value = it
        }
    }

}