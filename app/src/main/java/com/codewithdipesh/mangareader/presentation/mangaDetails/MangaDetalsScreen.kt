
package com.codewithdipesh.mangareader.presentation.mangaDetails

import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.codewithdipesh.mangareader.R
import com.codewithdipesh.mangareader.domain.model.Manga
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.codewithdipesh.mangareader.presentation.elements.ChapterCard
import com.codewithdipesh.mangareader.presentation.elements.MangaContent
import com.codewithdipesh.mangareader.presentation.elements.ToggleContent
import com.codewithdipesh.mangareader.ui.theme.japanese
import com.codewithdipesh.mangareader.ui.theme.regular
import com.skydoves.cloudy.cloudy

@RequiresApi(Build.VERSION_CODES.S)
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
    val context = LocalContext.current

    // Initial load effect
    LaunchedEffect(mangaId, coverImage, title) {
        if(mangaId.isNotEmpty() && coverImage.isNotEmpty() && title.isNotEmpty()) {
            viewModel.load(mangaId, coverImage, title, authorId)
        }
    }

    // Separate connectivity observer effect
    LaunchedEffect(Unit) {
        if(mangaId.isNotEmpty() && coverImage.isNotEmpty() && title.isNotEmpty()) {
            viewModel.observeConnectivity(mangaId, coverImage, title, authorId)
        }
    }
    //toast if error
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { message ->
            Log.e("event", message)
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
    //pagination
    LaunchedEffect(scrollState.value) {
        val scrollPosition = scrollState.value
        val maxScroll = scrollState.maxValue

        if(scrollPosition >= maxScroll *0.90 && !state.endReached){
            viewModel.getChapters(state.id)
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
            .background(color = colorResource(R.color.dark_gray))
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.Start
    ){

        //upper content with background
        Box(modifier = Modifier.fillMaxWidth()
            .wrapContentHeight()
        ){
            //background
            //blur
            Box(
                modifier =Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ){
                AsyncImage(
                    model = coverImage,
                    contentDescription = title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                        .blur(50.dp)
                )
            }
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(318.dp)
                    .background( //gradient
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.5f), // Darker Blur at the Top
                                colorResource(R.color.dark_gray), // Blend into Dark Gray

                            )
                        )
                    )
            )

            //content
            Box(Modifier
                .fillMaxWidth()
                .padding(top = 50.dp)
                .wrapContentHeight()
            ){
                Column {
                    //back button
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .offset(x=24.dp)
                            .background(color = colorResource(R.color.dark_gray))
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

                    //image and title other detail
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

                        //title and altTitle and author
                        Column(
                            modifier = Modifier.padding(start = 8.dp, top = 8.dp)
                        ) {
                            //title
                            Text(
                                text =
                                  if(state.title.length > 30 ){
                                    state.title.take(30) + "..."
                                  }else{
                                    state.title
                                },
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
                                    fontFamily = regular,
                                    fontWeight = FontWeight.Normal
                                )
                            )
                            Spacer(Modifier.height(8.dp))
                            //author
                            Text(
                                text = "By " + (state.author ?:""),
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    fontFamily = regular,
                                    color = Color.Gray,
                                    fontWeight = FontWeight.Normal
                                )

                            )
                            if(title.length < 20){
                                Spacer(Modifier.height(50.dp))
                            }else{
                                Spacer(Modifier.height(24.dp))
                            }
                            //Button
                            Box(
                                modifier = Modifier
                                    .size(160.dp,38.dp)
                                    .background(colorResource(R.color.yellow))
                                    .clickable {
                                        if(state.isInternetAvailable){
                                            //todo
                                        }else{
                                            viewModel.sendEvent("No Internet Connection")
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ){
                                Icon(
                                    painter = painterResource(R.drawable.read_now_icon),
                                    contentDescription = "Read Now",
                                    tint = Color.Black
                                )
                            }

                        }


                    }
                }
            }

        }

        Spacer(Modifier.height(16.dp))

        //select content option
        ToggleContent(
            list = listOf(
                MangaContent.Chapter,
                MangaContent.Details,
                MangaContent.Similar,
            ),
            state = state,
            onClick = {
                viewModel.changeSelectedContent(it)
            }
        )
        Spacer(Modifier.height(24.dp))
        //desc
        //and if title is longer add here
        if(state.selectedContent == MangaContent.Details){
            if(state.isLoading){
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(
                        color = colorResource(R.color.yellow),
                        strokeWidth = 2.dp,
                        modifier = Modifier
                            .size(50.dp)
                    )
                }
            }
            if(state.title.length > 30){
                Text(
                    text =state.title,
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 18.sp,
                        fontFamily = regular,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .padding(horizontal = 16.dp)

                )
                //Spacer
                Spacer(Modifier.height(16.dp))
            }
            //desc
            Text(
                text =
                    if(state.title.length > 30){
                       val desc = state.description ?: ""
                       state.title + "\n" + desc
                    }else{
                        state.description ?: ""
                    },
                color = Color.White,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 80.dp),
                textAlign = TextAlign.Start
            )
        }

        //chapters
        else if (state.selectedContent == MangaContent.Chapter){
            state.chapters.forEach {
                ChapterCard(
                    chapter = it,
                    onClick = {
                        if(state.isInternetAvailable){
                            //todo
                        }else{
                            viewModel.sendEvent("No Internet Connection")
                        }
                    }
                )
            }
            //chapter loading
            if(state.isChapterLoading){
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(
                        color = colorResource(R.color.yellow),
                        strokeWidth = 2.dp,
                        modifier = Modifier
                            .size(50.dp)
                    )
                }
            }
            //Spacer
            Spacer(Modifier.height(60.dp))
        }


    }



}
