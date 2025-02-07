package com.codewithdipesh.mangareader.presentation.homescreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codewithdipesh.mangareader.R
import com.codewithdipesh.mangareader.presentation.elements.SwipingCardAnimation
import com.codewithdipesh.mangareader.presentation.elements.dottedBackground
import com.codewithdipesh.mangareader.ui.theme.japanese
import com.codewithdipesh.mangareader.ui.theme.regular

@Composable
fun HomeScreen(
    viewmodel: HomeViewmodel,
    modifier: Modifier = Modifier
) {
    val state by viewmodel.state.collectAsState()

//    LaunchedEffect(Unit){
//        viewmodel.getTopManga()
//    }

    Box(modifier = Modifier
            .dottedBackground()
            .fillMaxSize()
    ){
       Column(
           modifier=Modifier.fillMaxSize()
               .padding(top = 50.dp, bottom = 32.dp),
           horizontalAlignment = Alignment.CenterHorizontally
       ){
           //suggestion
           Row (
               modifier = Modifier
                   .fillMaxWidth()
                   .padding(16.dp),
               horizontalArrangement = Arrangement.spacedBy(8.dp),
               verticalAlignment = Alignment.CenterVertically
           ){
               Text(
                   text = stringResource(R.string.suggestion),
                   color = Color.White,
                   fontSize = 26.sp,
                   fontFamily = japanese
               )
               Box(
                   modifier = Modifier
                       .size(100.dp,50.dp),
                   contentAlignment = Alignment.Center
               ){
                   Image(
                       painter = painterResource(R.drawable.speech_bubble),
                       contentDescription = "",
                   )
                   Text(
                       text = stringResource(R.string.top_5),
                       color = Color.Black,
                       fontSize = 16.sp,
                       fontFamily = japanese
                   )
               }
           }
           Spacer(Modifier.height(8.dp))
           //swiping cards
           SwipingCardAnimation(
               mangaList = state.topMangaList,
           )
           //all mangas



       }
    }



}