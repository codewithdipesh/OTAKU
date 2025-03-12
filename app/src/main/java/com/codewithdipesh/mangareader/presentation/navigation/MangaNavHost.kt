package com.codewithdipesh.mangareader.presentation.navigation

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.codewithdipesh.mangareader.presentation.downloads.DownloadScreen
import com.codewithdipesh.mangareader.presentation.downloads.DownloadedMangaScreen
import com.codewithdipesh.mangareader.presentation.downloads.DownloadsViewModel
import com.codewithdipesh.mangareader.presentation.favourites.FavouriteScreen
import com.codewithdipesh.mangareader.presentation.favourites.FavouritesViewModel
import com.codewithdipesh.mangareader.presentation.homescreen.HomeScreen
import com.codewithdipesh.mangareader.presentation.homescreen.HomeViewmodel
import com.codewithdipesh.mangareader.presentation.homescreen.SearchScreen
import com.codewithdipesh.mangareader.presentation.mangaDetails.MangaDetailsScreen
import com.codewithdipesh.mangareader.presentation.mangaDetails.MangaDetailsViewModel
import com.codewithdipesh.mangareader.presentation.reader.ReaderScreen
import com.codewithdipesh.mangareader.presentation.reader.ReaderViewModel

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun MangaNavHost(
    modifier: Modifier = Modifier,
    navController : NavHostController,
    homeViewmodel: HomeViewmodel,
    mangaViewModel : MangaDetailsViewModel,
    readerViewModel : ReaderViewModel,
    downloadViewModel : DownloadsViewModel,
    favouritesViewModel: FavouritesViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        enterTransition = {
            EnterTransition.None
        },
        exitTransition = {
            ExitTransition.None
        },
        popEnterTransition = {
            EnterTransition.None
        },
        popExitTransition = {
            ExitTransition.None
        },
    ) {
        composable(Screen.Home.route)
        {
            HomeScreen(
                viewmodel = homeViewmodel,
                navController =  navController
            )
        }
        composable(
            Screen.Search.route
        ){
            SearchScreen(
                viewmodel = homeViewmodel,
                navController =  navController
            )
        }
        composable(
            Screen.Detail.route,
            arguments = listOf(
                navArgument("mangaId") { type = NavType.StringType },
                navArgument("coverImage") { type = NavType.StringType },
                navArgument("title") { type = NavType.StringType },
                navArgument("authorId") { type = NavType.StringType }
            )
        ) { entry ->
            val mangaId = entry.arguments?.getString("mangaId") ?: ""
            val coverImage = entry.arguments?.getString("coverImage")?.let { Uri.decode(it) } ?: ""
            val title = entry.arguments?.getString("title")?.let { Uri.decode(it) } ?: ""
            val authorId = entry.arguments?.getString("authorId")?.let { Uri.decode(it) } ?: ""

            MangaDetailsScreen(
                 navController = navController,
                 viewModel = mangaViewModel,
                 mangaId = mangaId,
                 coverImage = coverImage,
                 title = title,
                 authorId = authorId
             )

        }
        composable(
            Screen.Reader.route,
            arguments = listOf(
                navArgument("chapterId") { type= NavType.StringType}
            )
        ){entry->
            val chapterId = entry.arguments?.getString("chapterId") ?: ""

            ReaderScreen(
                chapterId = chapterId,
                viewModel = readerViewModel,
                navController = navController,
                detailsViewModel = mangaViewModel
            )
        }

        composable(Screen.Favourites.route){
            FavouriteScreen(
                viewModel = favouritesViewModel,
                navController = navController
            )
        }
        composable(Screen.Downloads.route){
           DownloadScreen(
               viewModel = downloadViewModel,
               navController = navController
           )
        }
        composable(
            Screen.DownloadedManga.route,
            arguments = listOf(
                navArgument("mangaId") { type= NavType.StringType},
                navArgument("mangaName") { type= NavType.StringType}
            )
        ){
            val mangaId = it.arguments?.getString("mangaId") ?: ""
            val mangaName = it.arguments?.getString("mangaName") ?: ""
            DownloadedMangaScreen(
                mangaId = mangaId,
                mangaName = mangaName,
                navController = navController,
                viewModel = downloadViewModel
            )
        }
        composable(
            Screen.DownloadedReader.route,
            arguments = listOf(
                navArgument("chapterId") { type= NavType.StringType}
            )
        ){
            val chapterId = it.arguments?.getString("chapterId") ?: ""
            ReaderScreen(
                downloadedChapterId = chapterId,
                viewModel = downloadViewModel,
                navController = navController
            )
        }
    }

}