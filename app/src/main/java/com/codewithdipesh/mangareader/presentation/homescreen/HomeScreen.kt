package com.codewithdipesh.mangareader.presentation.homescreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.codewithdipesh.mangareader.presentation.elements.SwipingCardAnimation
import com.codewithdipesh.mangareader.presentation.elements.dottedBackground

@Composable
fun HomeScreen(
    viewmodel: HomeViewmodel,
    modifier: Modifier = Modifier
) {
    val state by viewmodel.state.collectAsState()

    LaunchedEffect(Unit){
        viewmodel.getTopManga()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .dottedBackground(),
        contentAlignment = Alignment.Center
    ){
       SwipingCardAnimation(
           mangaList = state.topMangaList
       )
    }



}