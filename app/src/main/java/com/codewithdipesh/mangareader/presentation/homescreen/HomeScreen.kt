package com.codewithdipesh.mangareader.presentation.homescreen

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowOverflow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.overscroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.codewithdipesh.mangareader.R
import com.codewithdipesh.mangareader.presentation.elements.BottomNav
import com.codewithdipesh.mangareader.presentation.elements.MangaCard
import com.codewithdipesh.mangareader.presentation.elements.SearchBar
import com.codewithdipesh.mangareader.presentation.elements.SwipingCardAnimation
import com.codewithdipesh.mangareader.presentation.elements.TopMangaSkeleton
import com.codewithdipesh.mangareader.presentation.elements.dottedBackground
import com.codewithdipesh.mangareader.presentation.navigation.Screen
import com.codewithdipesh.mangareader.ui.theme.japanese
import com.codewithdipesh.mangareader.ui.theme.regular
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.HomeScreen(
    viewmodel: HomeViewmodel,
    modifier: Modifier = Modifier,
    navController: NavController,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val state by viewmodel.state.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val shimmerColors = listOf(
        Color.DarkGray,
        Color.Gray,
        Color.DarkGray
    )


    val transition = rememberInfiniteTransition()

    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            )
        )
    )
    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x= translateAnim.value,y=translateAnim.value)
    )

    LaunchedEffect(Unit) {
        viewmodel.uiEvent.collect { message ->
            Log.e("event", message)
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    Box(
        modifier=Modifier.fillMaxSize()
    ){
        //screen component
        Column(
            modifier= Modifier
                .fillMaxSize()
                .dottedBackground()
                .padding(top = 50.dp, bottom = 32.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            //search bar
            SearchBar(
                enabled = false,
                onClick = {
                    navController.navigate(Screen.Search.route)
                }
            )

            //suggestion text
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
            if(state.topMangaList.isEmpty()){
                TopMangaSkeleton(brush= brush)
            }else{
                SwipingCardAnimation(
                    mangaList = state.topMangaList,
                    onClick = {
                        navController.navigate(Screen.Detail.createRoute(it))
                    },
                    animatedVisibilityScope = animatedVisibilityScope,
                    onSuccessSwipe = { viewmodel.updateTopMangaList(it)}
                )
            }

            //all mangas text
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = stringResource(R.string.all_manga),
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontFamily = japanese
                )
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .background(color = Color.Black),
                    contentAlignment = Alignment.Center
                ){
                    Image(
                        painter = painterResource(R.drawable.show_more_icon),
                        contentDescription = "",
                    )
                }
            }

            //mangas
            FlowRow (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                maxItemsInEachRow = 6,
                overflow = FlowRowOverflow.Clip,
                horizontalArrangement = Arrangement.SpaceAround,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ){
                //skeleton
                if(state.allMangas.isEmpty()){
                    listOf(1,2,3,4).forEach {
                        MangaCard(
                            manga = null,
                            modifier = Modifier
                                .padding(4.dp)
                                .background(brush)
                        )
                    }
                }
                state.allMangas.forEach {
                    MangaCard(
                        manga = it,
                        modifier = Modifier.padding(4.dp),
                        onClick = {
                            navController.navigate(Screen.Detail.createRoute(it))
                        }
                    )
                }
            }


        }

    }


}