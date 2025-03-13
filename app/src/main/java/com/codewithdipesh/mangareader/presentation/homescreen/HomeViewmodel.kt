package com.codewithdipesh.mangareader.presentation.homescreen

import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codewithdipesh.mangareader.domain.observer.connectivityObserver
import com.codewithdipesh.mangareader.domain.repository.MangaRepository
import com.codewithdipesh.mangareader.domain.util.AppError
import com.codewithdipesh.mangareader.domain.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewmodel @Inject constructor(
    private val repository : MangaRepository,
    private val connectivity : connectivityObserver
):ViewModel(){

    private val _state = MutableStateFlow(HomeUiState())
    val state = _state.asStateFlow()

    private var isDataInitialized = false

    private var lastEmitted = ""

    private val _uiEvent = Channel<String>(Channel.BUFFERED) // Buffered so it doesn't block
    val uiEvent = _uiEvent.receiveAsFlow()

    private var searchJob: Job? = null
    private var clearJob : Job? = null //its for clearing lastEmitted error


    init {
        viewModelScope.launch(Dispatchers.IO){
            loadhistory()
        }
        observeConnectivity()
    }

    fun initializeDataIfNeeded() {
        if (!isDataInitialized) {
            refetchData()
            isDataInitialized = true
        }
    }

    fun observeConnectivity(){
        viewModelScope.launch(Dispatchers.IO) {
            connectivity.observe().collect {
                when (it) {
                    connectivityObserver.Status.Available -> {
                        _state.value = _state.value.copy(
                            isInternetAvailable = true
                        )
                        if(_state.value.hasErrorOccured){//available after any error
                            setNoErrorState()
                            sendEvent("Internet restored! Fetching data...")
                            refetchData()
                        }
                    }
                    connectivityObserver.Status.UnAvailable,connectivityObserver.Status.Lost -> {
                        _state.value = _state.value.copy(
                            isInternetAvailable = false
                        )
                    }
                }
            }
        }
    }

    private fun setErrorState(){
        _state.value = _state.value.copy(
            hasErrorOccured = true
        )
    }
    private fun setNoErrorState(){
        _state.value = _state.value.copy(
            hasErrorOccured = false
        )
    }

    private fun refetchData() {
        Log.d("mainscreen", "refetched")
        // Separate launch calls for better concurrency
        if (_state.value.topMangaList.isEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                getTopManga()
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
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
                setNoErrorState()
            }
            is Result.Error ->{
                setErrorState()
                sendEvent(result.error.message)
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
                setNoErrorState()
            }
            is Result.Error ->{
                setErrorState()
                sendEvent(result.error.message)
                Log.d("HomeViewmodel", "getTopManga: Error ${result.error}")
            }
        }
    }

    fun onChangeSearchValue(value : String){
        searchJob?.cancel()
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

    fun searchManga(){
        if(_state.value.searchValue == ""){
            Log.d("HomeViewmodel", "searchManga: Error")
            sendEvent("Pls enter anything")
        }else{
            _state.value = _state.value.copy(
                isloading = true
            )
            searchJob?.cancel()
            searchJob = viewModelScope.launch {
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
                        if(result.error !is AppError.UnknownError) sendEvent(result.error.message)
                        _state.value = _state.value.copy(
                            isloading = false
                        )
                        Log.d("HomeViewmodel", "searchManga: Error ${result.error}")
                    }

                }
            }

        }
    }

    fun sendEvent(message: String) {
        if(lastEmitted != message){
            viewModelScope.launch {
                _uiEvent.send(message)
            }
            lastEmitted = message

            clearJob?.cancel()
            clearJob = viewModelScope.launch {
                delay(5000)
                lastEmitted =""
            }
        }
    }




}