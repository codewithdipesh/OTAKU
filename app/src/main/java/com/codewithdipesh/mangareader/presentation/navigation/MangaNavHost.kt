package com.codewithdipesh.mangareader.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.codewithdipesh.mangareader.presentation.homescreen.HomeScreen
import com.codewithdipesh.mangareader.presentation.homescreen.HomeViewmodel
import com.codewithdipesh.mangareader.presentation.homescreen.SearchScreen

@Composable
fun MangaNavHost(
    modifier: Modifier = Modifier,
    navController : NavHostController,
    homeViewmodel: HomeViewmodel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        enterTransition = {
            fadeIn(animationSpec = tween(100))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(100))
        },
        popEnterTransition = {
            fadeIn(animationSpec = tween(100))
        },
        popExitTransition = {
            fadeOut(animationSpec = tween(100))
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
    }

}