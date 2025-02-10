package com.codewithdipesh.mangareader.presentation.navigation

sealed class Screen(val route : String){
    object Home : Screen("home")
    object Search : Screen("search")
    object Detail : Screen("details/{mangaId}"){
        fun createRoute(mangaId : String) = "details/${mangaId}"
    }
}