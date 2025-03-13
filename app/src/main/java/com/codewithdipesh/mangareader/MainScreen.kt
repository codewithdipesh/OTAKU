package com.codewithdipesh.mangareader

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.codewithdipesh.mangareader.presentation.downloads.DownloadsViewModel
import com.codewithdipesh.mangareader.presentation.elements.BottomNav
import com.codewithdipesh.mangareader.presentation.favourites.FavouritesViewModel
import com.codewithdipesh.mangareader.presentation.homescreen.HomeViewmodel
import com.codewithdipesh.mangareader.presentation.mangaDetails.MangaDetailsViewModel
import com.codewithdipesh.mangareader.presentation.navigation.MangaNavHost
import com.codewithdipesh.mangareader.presentation.navigation.Screen
import com.codewithdipesh.mangareader.presentation.reader.ReaderViewModel

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    homeViewModel : HomeViewmodel,
    mangaViewModel : MangaDetailsViewModel,
    readerViewModel : ReaderViewModel,
    downloadViewModel : DownloadsViewModel,
    favouritesViewModel:FavouritesViewModel
) {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry
        ?.destination
        ?.route

    val showBottomBar = currentRoute in listOf(
        Screen.Home.route,
        Screen.Favourites.route,
        Screen.Downloads.route
    )

    LaunchedEffect(currentRoute) {
        if (currentRoute == Screen.Home.route) {
            homeViewModel.initializeDataIfNeeded()
        }
    }

    Box(Modifier.fillMaxSize( )
    ){
        MangaNavHost(
            navController = navController,
            homeViewmodel = homeViewModel,
            mangaViewModel = mangaViewModel,
            readerViewModel = readerViewModel,
            downloadViewModel = downloadViewModel,
            favouritesViewModel =favouritesViewModel
        )

        //bottom bar
        if(showBottomBar){
            Log.d("mainscreen","show bottom")
            Box(
                modifier = Modifier.wrapContentSize()
                    .align(Alignment.BottomCenter)
                    .offset(y = (-46).dp)
            ){
                BottomNav(
                    selectedOption = when(navController.currentBackStackEntry?.destination?.route){
                        Screen.Home.route -> Screen.Home
                        Screen.Favourites.route -> Screen.Favourites
                        Screen.Downloads.route -> Screen.Downloads
                        else ->Screen.Detail //for other screen though no use of thi screen , only using the top 3
                    },
                    onClick = {
                        navController.navigate(it.route) {
                            launchSingleTop = true //prevent multiple copies
                            popUpTo(navController.graph.startDestinationId){
                                saveState = true //Clear up to the start destination
                            }
                            restoreState = true
                        }

                    }
                )
            }
        }

    }

}