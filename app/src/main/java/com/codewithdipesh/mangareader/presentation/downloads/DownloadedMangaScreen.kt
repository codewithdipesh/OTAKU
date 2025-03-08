package com.codewithdipesh.mangareader.presentation.downloads

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.codewithdipesh.mangareader.R
import com.codewithdipesh.mangareader.domain.model.Chapter
import com.codewithdipesh.mangareader.presentation.elements.BottomNav
import com.codewithdipesh.mangareader.presentation.elements.ChapterCard
import com.codewithdipesh.mangareader.presentation.elements.DownloadedMangaCard
import com.codewithdipesh.mangareader.presentation.navigation.Screen
import com.codewithdipesh.mangareader.ui.theme.japanese
import com.codewithdipesh.mangareader.ui.theme.regular

@Composable
fun DownloadedMangaScreen(
    modifier: Modifier = Modifier,
    mangaId : String,
    viewModel: DownloadsViewModel,
    navController: NavController,
){
    val state by viewModel.state.collectAsState()
    val mangaState by viewModel.mangaState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getDownloadedManga(mangaId)
    }

    Box(modifier = Modifier.fillMaxSize()){
        Column(
            modifier = Modifier.fillMaxSize()
                .background(color = colorResource(R.color.dark_gray)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            //loading
            if(state.isLoading){
                CircularProgressIndicator(
                    color = colorResource(R.color.yellow),
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(50.dp)
                )
            }
            //UpperRow
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 50.dp, bottom = 32.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ){
                //back icon
                IconButton(
                    onClick = {navController.navigateUp()}
                ) {
                    Icon(
                        painter = painterResource(R.drawable.back_nav_icon),
                        tint = Color.White,
                        contentDescription = "back to manga ${mangaState.title}"
                    )
                }
                Spacer(Modifier.width(20.dp))
                //manga Name
                Text(
                    text = mangaState.title.take(50),
                    style = TextStyle(
                        color = Color.White,
                        fontFamily = regular,
                        fontSize = 16.sp
                    )
                )
            }
            //chapterList
            mangaState.chapters.forEach {
                ChapterCard(
                    chapter = it,
                    onClick = {
                        navController.navigate(Screen.Reader.createRoute(it))
                    }
                )
            }

        }

    }
}