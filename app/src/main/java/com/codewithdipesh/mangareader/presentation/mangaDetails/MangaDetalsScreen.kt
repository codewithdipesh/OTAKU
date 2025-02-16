package com.codewithdipesh.mangareader.presentation.mangaDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.codewithdipesh.mangareader.R
import com.codewithdipesh.mangareader.domain.model.Manga
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.codewithdipesh.mangareader.ui.theme.japanese
import com.codewithdipesh.mangareader.ui.theme.regular

@Composable
fun MangaDetailsScreen(
    mangaId : String,
    coverImage : String,
    title : String,
    authorId : String,
    viewModel: MangaDetailsViewModel,
    navController : NavController,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        if(mangaId != "" && coverImage != "" && title!= ""){
            viewModel.load(mangaId,coverImage,title,authorId)
        }else{
            //todo error
        }
    }
    DisposableEffect(Unit){
        onDispose {
            viewModel.clearUi()
        }
    }

    Column(
        modifier=modifier
            .fillMaxSize()
            .background(colorResource(R.color.dark_gray) )
            .padding(top = 50.dp, bottom = 32.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.Start
    ){
        //back button
        Box(
            modifier = Modifier
                .size(50.dp)
                .offset(x=24.dp)
                .background(color = colorResource(R.color.medium_gray))
                .clickable {
                    navController.navigateUp()
                },
            contentAlignment = Alignment.Center
        ){
            Icon(
                painter = painterResource(R.drawable.back_nav_icon),
                contentDescription = "close search bar",
                tint = Color.White
            )
        }
        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.Top
        ) {
            //Image
            Box(
                modifier = modifier
                    .width(180.dp)
                    .height(270.dp) //height and weight is in  2:3 ratio
            ){
                if (coverImage != "") {
                    AsyncImage(
                        model = coverImage,
                        contentDescription = title,
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(R.drawable.defaultmangacover),
                        error = painterResource(R.drawable.defaultmangacover),
                    )
                }
            }

            //title and altTitle
            Column(
                modifier = Modifier.padding(start = 8.dp, top = 8.dp)
            ) {
                //title
                Text(
                    text = state.title,
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 22.sp,
                        fontFamily = regular,
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(Modifier.height(8.dp))
                //alt title
                Text(
                    text = if(state.altTitle == null) ""
                    else state.altTitle!!,
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 14.sp,
                        fontFamily = japanese,
                        fontWeight = FontWeight.Normal
                    )
                )
                Spacer(Modifier.height(38.dp))
                Text(
                    text = "By " + (state.author ?:""),
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = regular,
                        color = Color.Gray,
                        fontWeight = FontWeight.Normal
                    )

                )

            }


        }
        Text(
            text = state.description?:"",
            color = Color.White
        )
        state.chapters.forEach {
            Text(
                text = it.chapterNumber.toString(),
                color = Color.White
            )
            Text(
                text = it.title ?: "No title",
                color = Color.White
            )
        }


    }


}