
package com.codewithdipesh.mangareader.presentation.mangaDetails

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.Down
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.Up
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.BoundsTransform
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.animation.with
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.codewithdipesh.mangareader.R
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.currentBackStackEntryAsState
import com.codewithdipesh.mangareader.domain.model.Rating
import com.codewithdipesh.mangareader.domain.model.Status
import com.codewithdipesh.mangareader.domain.util.DisplayUtils
import com.codewithdipesh.mangareader.presentation.elements.ChapterCard
import com.codewithdipesh.mangareader.presentation.elements.MangaCard
import com.codewithdipesh.mangareader.presentation.elements.MangaContent
import com.codewithdipesh.mangareader.presentation.elements.OrderChooser
import com.codewithdipesh.mangareader.presentation.elements.TinyCard
import com.codewithdipesh.mangareader.presentation.elements.ToggleContent
import com.codewithdipesh.mangareader.presentation.navigation.Screen
import com.codewithdipesh.mangareader.ui.theme.regular
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class, ExperimentalSharedTransitionApi::class,
    ExperimentalAnimationApi::class
)
@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun SharedTransitionScope.MangaDetailsScreen(
    mangaId : String,
    coverImage : String,
    title : String,
    authorId : String,
    viewModel: MangaDetailsViewModel,
    navController : NavController,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()
    var scrollState = rememberScrollState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var needToClearUi by remember { mutableStateOf(false) }
    // Initial load effect
    LaunchedEffect(mangaId, coverImage, title) {
        if(mangaId.isNotEmpty() && coverImage.isNotEmpty() && title.isNotEmpty()) {
            viewModel.load(mangaId, coverImage, title, authorId)
        }
    }
    BackHandler {
        navController.navigate(Screen.Home.route) {
            popUpTo(0)
            launchSingleTop = true
        }
        needToClearUi = true
    }
    DisposableEffect(Unit) {
        onDispose {
            if (needToClearUi) {
                viewModel.clearUi()
            }
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
        if(!state.hasToggledOnce) return@LaunchedEffect //skip for the first time toggle bcz it automatically paging

        val scrollPosition = scrollState.value
        val maxScroll = scrollState.maxValue

        val isAscOrder = state.selectedSortOrder == "asc"
        if(scrollPosition >= maxScroll *0.90){
            if(isAscOrder && !state.endReachedAsc) viewModel.getChapters(state.id)
            else if(!isAscOrder && !state.endReachedDesc) viewModel.getChapters(state.id)
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
                                navController.navigate(Screen.Home.route) {
                                    popUpTo(0)
                                    launchSingleTop = true
                                }
                                needToClearUi = true
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
                                .clip(RoundedCornerShape(16.dp))
                                .sharedBounds(
                                    rememberSharedContentState(key = "mangaImage/${coverImage}"),
                                    animatedVisibilityScope = animatedVisibilityScope,
                                    enter = fadeIn(),
                                    exit = ExitTransition.None,
                                    resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds(),
                                    boundsTransform = BoundsTransform { _, _ ->
                                        tween(durationMillis = 150)
                                    }
                                )
                        ){
                            if (coverImage != "") {
                                AsyncImage(
                                    model = coverImage,
                                    contentDescription = title,
                                    contentScale = ContentScale.Crop,
                                    placeholder = painterResource(R.drawable.defaultmangacover),
                                    error = painterResource(R.drawable.defaultmangacover),
                                    modifier = Modifier.clip(RoundedCornerShape(16.dp))
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
                                      else{
                                          if(state.altTitle!!.length > 30) state.altTitle!!.take(30) + "..."
                                          else state.altTitle!!
                                },
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
                                            //add to favourites
                                            if(state.isFavourite) viewModel.removeFromFavourites()
                                            else viewModel.addToFavourites()
                                        }else{
                                            viewModel.sendEvent("No Internet Connection")
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ){
                                Text(
                                    text = if(state.isFavourite) "REMOVE FROM FAV" else "ADD TO FAV",
                                    style = TextStyle(
                                        color = Color.Black,
                                        fontSize = 10.sp,
                                        fontFamily = regular,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                            //publication details
                            if(state.status != null){
                                Row(modifier =Modifier.fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ){
                                    //color according to teh state
                                    Box(Modifier
                                        .size(10.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (state.status == Status.Ongoing) Color.Green else colorResource(R.color.blue))
                                    )
                                    //current state
                                    Text(
                                        text = "${state.year},${state.status!!.name.uppercase(
                                            Locale.getDefault()
                                        )}",
                                        style = TextStyle(
                                            color= Color.White,
                                            fontFamily = regular,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 10.sp
                                        )
                                    )
                                }
                            }
                        }


                    }
                }
            }

        }
        Spacer(Modifier.height(6.dp))
        //showing the attribute list
        if(state.genres.isNotEmpty()){
            FlowRow(
                maxItemsInEachRow = 8,
                modifier = Modifier
                    .fillMaxWidth().padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.Start
            ){
                //contentRating
                TinyCard(
                    text = if (state.contentRating == Rating.Pornographic) "18+"
                    else state.contentRating.name,
                    bgColor = if (state.contentRating == Rating.Pornographic) Color.Red
                    else if(state.contentRating == Rating.Erotica) colorResource(R.color.orange)
                    else Color.DarkGray,
                    textSize = 14

                )
                //list of genres
                state.genres.take(7).forEach {
                    TinyCard(text = it.entries.first().value,textSize = 14)//id->name so value will be the name
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
                scope.launch {
                    viewModel.changeSelectedContent(it)
                    scrollState.animateScrollTo(0)
                }
            }
        )
        Spacer(Modifier.height(8.dp))
        //desc
        //and if title is longer add here
        AnimatedContent(
            targetState = state.selectedContent
        ) {targetState->
            when(targetState){
                //chapters
                MangaContent.Chapter -> {
                    Column(

                    ){
                        //sort by
                        Row(
                            modifier = Modifier.fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Text(
                                text="Sort Order",
                                style = TextStyle(
                                    color = Color.White,
                                    fontFamily = regular,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            )
                            Spacer(Modifier.width(8.dp))
                            OrderChooser(
                                orderType = state.selectedSortOrder,
                                onToggle = {
                                    scope.launch {
                                        viewModel.toggleSortOrder()
                                    }
                                }
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                        if(state.selectedSortOrder == "asc"){
                            if(!state.isChapterLoading && state.chaptersAsc.isEmpty() ){
                                Text(
                                    text = "No Chapter Found",
                                    style = TextStyle(
                                        color = Color.LightGray,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = regular,
                                        fontSize = 14.sp
                                    ),
                                    modifier = Modifier.padding()

                                )
                            }
                            state.chaptersAsc.forEach {
                                ChapterCard(
                                    chapter = it,
                                    onClick = {
                                        if(state.isInternetAvailable){
                                            navController.navigate(Screen.Reader.createRoute(it))
                                        }else{
                                            viewModel.sendEvent("No Internet Connection")
                                        }
                                    }
                                )
                            }
                        }else{
                            if(!state.isChapterLoading && state.chaptersDesc.isEmpty() ){
                                Text(
                                    text = "No Chapter Found",
                                    style = TextStyle(
                                        color = Color.LightGray,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = regular,
                                        fontSize = 14.sp
                                    ),
                                    modifier = Modifier.padding()

                                )
                            }
                            state.chaptersDesc.forEach {
                                ChapterCard(
                                    chapter = it,
                                    onClick = {
                                        if(state.isInternetAvailable){
                                            navController.navigate(Screen.Reader.createRoute(it))
                                        }else{
                                            viewModel.sendEvent("No Internet Connection")
                                        }
                                    }
                                )
                            }
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
                //details
                MangaContent.Details -> {
                    Column(

                    ){
                        Spacer(Modifier.height(16.dp))
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
                }
                MangaContent.Similar -> {
                    Spacer(Modifier.height(16.dp))
                    if(state.similarMangas.isEmpty() && !state.isLoading){
                        Text(
                            text = "No Manga Found",
                            style = TextStyle(
                                color = Color.LightGray,
                                fontWeight = FontWeight.Bold,
                                fontFamily = regular,
                                fontSize = 14.sp
                            ),
                            modifier = Modifier.padding()

                        )
                    }else if (state.similarMangas.isNotEmpty()){
                        val columns = DisplayUtils.calculateGridColumns()
                        val chunkedMangaList = state.similarMangas.chunked(columns)

                        Column(

                        ) { Spacer(Modifier.height(16.dp))
                            chunkedMangaList.forEach { rowMangas ->
                                Row(
                                    modifier = Modifier.fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceAround
                                ) {
                                    rowMangas.forEach { manga ->
                                        Log.d("SimilarManga", manga.coverImage.toString())
                                        MangaCard(
                                            manga = manga,
                                            onClick = {
                                                if (state.isInternetAvailable) {
                                                    viewModel.clearUi()
                                                    navController.navigate(Screen.Detail.createRoute(manga))

                                                } else {
                                                    viewModel.sendEvent("No Internet ")
                                                }
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }else{
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

                }
            }
        }


    }


}
