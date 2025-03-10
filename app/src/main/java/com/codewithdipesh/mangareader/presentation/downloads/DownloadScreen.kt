package com.codewithdipesh.mangareader.presentation.downloads

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.codewithdipesh.mangareader.R
import com.codewithdipesh.mangareader.presentation.elements.BottomNav
import com.codewithdipesh.mangareader.presentation.elements.DownloadedMangaCard
import com.codewithdipesh.mangareader.presentation.navigation.Screen
import com.codewithdipesh.mangareader.ui.theme.japanese

@Composable
fun DownloadScreen(
    modifier: Modifier = Modifier,
    viewModel : DownloadsViewModel,
    navController: NavController
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit){
        viewModel.getDownloads()
    }

    Box(modifier = Modifier.fillMaxSize()){
        Column(
            modifier = Modifier.fillMaxSize()
                .background(color = colorResource(R.color.dark_gray)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) { //UpperRow
            Text(
                text = stringResource(R.string.downloads),
                style = TextStyle(
                    color = Color.White,
                    fontFamily = japanese,
                    fontSize = 20.sp
                ),
                modifier = Modifier.padding(horizontal = 16.dp)
                    .padding(top = 50.dp, bottom = 32.dp)
            )
            //downloadList If have
            state.downloads.downloads.forEach{ (manga, chapterList) ->
                DownloadedMangaCard(
                    mangaDownloadedDetails = manga,
                    onClick = {
                        navController.navigate(Screen.DownloadedManga.createRoute(it))
                    }
                )
            }
            //loading
            if(state.isLoading){
                CircularProgressIndicator(
                    color = colorResource(R.color.yellow),
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(50.dp)
                )
            }

        }
    }
    
}