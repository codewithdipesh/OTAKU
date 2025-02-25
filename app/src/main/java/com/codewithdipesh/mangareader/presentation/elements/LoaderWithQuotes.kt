package com.codewithdipesh.mangareader.presentation.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import com.codewithdipesh.mangareader.R
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codewithdipesh.mangareader.domain.constants.MangaQuotes
import com.codewithdipesh.mangareader.ui.theme.regular
import kotlin.random.Random

@Composable
fun LoaderWithQuotes(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize()
            .background(color = Color.Black),
        contentAlignment = Alignment.Center
    ){
        //background iamge
        Image(
            painter = painterResource(R.drawable.manga_cloud),
            contentDescription = "Loading..",
            contentScale = ContentScale.Crop,
            modifier = Modifier.alpha(0.4f)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            //loader
            CircularProgressIndicator(
                color = colorResource(R.color.yellow),
                strokeWidth = 3.dp,
                modifier = Modifier
                    .size(60.dp)
            )
            Spacer(Modifier.height(8.dp))
            //loading
            Text(
                text = "Loading...",
                style = TextStyle(
                    color = Color.LightGray,
                    fontWeight = FontWeight.Normal,
                    fontFamily = regular,
                    fontSize = 14.sp
                ),
                textAlign = TextAlign.Center
            )
            //random quotes
            Text(
                text = "Fun fact: "+MangaQuotes.quotes[Random.nextInt(MangaQuotes.quotes.size)] ,//random quote
                style = TextStyle(
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontFamily = regular,
                    fontSize = 18.sp
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}