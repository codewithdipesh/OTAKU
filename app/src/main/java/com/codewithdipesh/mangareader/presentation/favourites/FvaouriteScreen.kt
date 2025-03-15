package com.codewithdipesh.mangareader.presentation.favourites

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowOverflow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
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
import com.codewithdipesh.mangareader.domain.util.DisplayUtils
import com.codewithdipesh.mangareader.presentation.elements.DownloadedMangaCard
import com.codewithdipesh.mangareader.presentation.elements.MangaCard
import com.codewithdipesh.mangareader.presentation.navigation.Screen
import com.codewithdipesh.mangareader.ui.theme.japanese
import com.codewithdipesh.mangareader.ui.theme.regular
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FavouriteScreen(
    modifier: Modifier = Modifier,
    viewModel: FavouritesViewModel,
    navController: NavController
){
    val mangaList by viewModel.mangaList.collectAsState()
    val scope = rememberCoroutineScope()
    val scrollstate = rememberScrollState()

    LaunchedEffect(Unit){
        viewModel.getFavourites()
    }

    Box(modifier = Modifier.fillMaxSize()){
        Column(
            modifier = Modifier.fillMaxSize()
                .background(color = colorResource(R.color.dark_gray)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) { //UpperRow
            Text(
                text = stringResource(R.string.favourites),
                style = TextStyle(
                    color = Color.White,
                    fontFamily = japanese,
                    fontSize = 20.sp
                ),
                modifier = Modifier.padding(horizontal = 16.dp)
                    .padding(top = 50.dp, bottom = 32.dp)
            )

            //manga list
            LazyVerticalGrid(
                columns = GridCells.Fixed(DisplayUtils.calculateGridColumns()),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ){
                items(mangaList){ manga ->
                    MangaCard(
                        manga = manga,
                        onClick = {
                            navController.navigate(Screen.Detail.createRoute(manga))
                        },
                        onFavouriteToggle = {
                            scope.launch { viewModel.toggleFavourite(manga) }
                        }
                    )
                }
            }
            if(mangaList.isEmpty()){
                Text(
                    text = "No Favourite Manga",
                    style = TextStyle(
                        color = Color.White,
                        fontFamily = regular,
                        fontSize = 14.sp
                    ),
                    modifier = Modifier.padding(32.dp)
                )
            }

        }
    }
}