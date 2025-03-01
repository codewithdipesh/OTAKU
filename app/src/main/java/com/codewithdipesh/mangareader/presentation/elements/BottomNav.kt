package com.codewithdipesh.mangareader.presentation.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.codewithdipesh.mangareader.R
import com.codewithdipesh.mangareader.presentation.navigation.Screen
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.hazeEffect

@Composable
fun BottomNav(
    modifier: Modifier = Modifier,
    widthSingleOption : Int = 70,
    heightSingleOption : Int = 60,
    selectedOption : Screen,
    onClick : (Screen)->Unit
) {
   Box(
       modifier = modifier
           .offset(x=14.dp)
           .width((widthSingleOption*3).dp)
           .height(heightSingleOption.dp)
           .clip(CustomButtonShape())
   ){

           Box(
               modifier = Modifier
                   .size(widthSingleOption.dp,heightSingleOption.dp)
                   .clip(CustomButtonShape())
                   .background(
                       if(selectedOption == Screen.Home || (selectedOption != Screen.Favourites && selectedOption != Screen.Downloads)) Color.Black
                       else colorResource(R.color.light_yellow).copy(0.7f)
                   )
                   .then(
                       if(selectedOption != Screen.Home){
                           Modifier.blur(20.dp)
                       }else Modifier
                   )
                   .align(Alignment.CenterStart)
                   .clickable {
                       onClick(Screen.Home)
                   },
               contentAlignment = Alignment.Center
           ){
               Icon(
                   painter = painterResource(R.drawable.home_icon),
                   contentDescription = "home",
                   tint = if(selectedOption == Screen.Home || (selectedOption != Screen.Favourites && selectedOption != Screen.Downloads))
                           Color.White
                          else colorResource(R.color.medium_gray)
               )
           }
           //favourites
           Box(
               modifier = Modifier
                   .offset(x=(-14.dp))
                   .size(widthSingleOption.dp,heightSingleOption.dp)
                   .clip(CustomButtonShape())
                   .background(
                       if(selectedOption == Screen.Favourites) Color.Black
                       else colorResource(R.color.light_yellow).copy(0.7f)
                   )
                   .then(
                       if(selectedOption != Screen.Favourites){
                           Modifier.blur(20.dp)
                       }else Modifier
                   )
                   .align(Alignment.Center)
                   .clickable {
                       onClick(Screen.Favourites)
                   },
               contentAlignment = Alignment.Center
           ){
               Icon(
                   painter = painterResource(R.drawable.favourite_icon),
                   contentDescription = "home",
                   tint = if(selectedOption == Screen.Favourites) Color.White
                   else colorResource(R.color.medium_gray)
               )
           }
           //downloads
           Box(
               modifier = Modifier
                   .offset(x=(-28.dp))
                   .size(widthSingleOption.dp,heightSingleOption.dp)
                   .clip(CustomButtonShape())
                   .background(
                       if(selectedOption == Screen.Downloads) Color.Black
                       else colorResource(R.color.light_yellow).copy(0.7f)
                   )
                   .then(
                       if(selectedOption != Screen.Downloads){
                           Modifier.blur(20.dp)
                       }else Modifier
                   )
                   .align(Alignment.CenterEnd)
                   .clickable {
                       onClick(Screen.Downloads)
                   },
               contentAlignment = Alignment.Center
           ){
               Icon(
                   painter = painterResource(R.drawable.downloads_icon),
                   contentDescription = "home",
                   tint = if(selectedOption == Screen.Downloads) Color.White
                   else colorResource(R.color.medium_gray)
               )
           }

   }
}