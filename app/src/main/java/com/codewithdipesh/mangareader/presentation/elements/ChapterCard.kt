package com.codewithdipesh.mangareader.presentation.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codewithdipesh.mangareader.R
import com.codewithdipesh.mangareader.domain.model.Chapter
import com.codewithdipesh.mangareader.domain.model.DownloadStatus
import com.codewithdipesh.mangareader.domain.model.DownloadedChapter
import com.codewithdipesh.mangareader.ui.theme.regular

@Composable
fun ChapterCard(
    modifier: Modifier = Modifier,
    chapter : Chapter,
    onClick : ()->Unit ={}
){
    Column(
        modifier.fillMaxWidth()
            .wrapContentHeight()
            .padding(bottom = 8.dp)
            .background(
                if(chapter.isVisited) colorResource(R.color.yellow).copy(alpha = 0.3f)
                else Color.Transparent
            )
            .clickable {
                onClick()
            }
    ) {
        //content
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(0.9f),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ){
                Text(
                    text = "Ch "+chapter.chapterNumber.toString(),
                    style = TextStyle(
                        color = Color.Gray,
                        fontFamily = regular,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp
                    )
                )
                Text(
                    text =
                    if(chapter.title.isNullOrEmpty()){
                        "Chapter " +chapter.chapterNumber.toString()
                    }else{
                        chapter.title
                    },
                    style = TextStyle(
                        color = Color.White,
                        fontFamily = regular,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                )
            }
            Icon(
                painter = painterResource(R.drawable.show_more_icon),
                contentDescription = "start reading +${
                    if(chapter.title.isNullOrEmpty()){
                        "Chapter " +chapter.chapterNumber.toString()
                    }else{
                        chapter.title
                    }}",
                tint = Color.White,
                modifier = Modifier.weight(0.1f)

            )
        }
        Spacer(Modifier.height(8.dp))
        //divider
        HorizontalDivider(
            color = Color.DarkGray,
            thickness = 1.dp
        )



    }

}

@Composable
fun ChapterCard(
    modifier: Modifier = Modifier,
    chapter : DownloadedChapter,
    onClick : ()->Unit ={}
){
    Column(
        modifier.fillMaxWidth()
            .wrapContentHeight()
            .padding(bottom = 8.dp)
            .clickable {
                if(chapter.status == DownloadStatus.Downloaded){
                    onClick()
                }
            }
    ) {
        //content
        Column(modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
        ){
                Text(
                    text = "Ch "+chapter.chapterNumber.toString(),
                    style = TextStyle(
                        color = Color.Gray,
                        fontFamily = regular,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp
                    )
                )
                Text(
                    text =
                    if(chapter.title.isNullOrEmpty()){
                        "Chapter " +chapter.chapterNumber.toString()
                    }else{
                        chapter.title
                    },
                    style = TextStyle(
                        color = Color.White,
                        fontFamily = regular,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                )
                if(chapter.status == DownloadStatus.Error()){
                    Text(
                        text = chapter.status.name,
                        style = TextStyle(
                            color = Color.Red,
                            fontFamily = regular,
                            fontWeight = FontWeight.Normal,
                            fontSize = 12.sp
                        )
                    )
                }
                if(chapter.status == DownloadStatus.Downloading){
                    Text(
                        text = "Downloading...",
                        style = TextStyle(
                           color = colorResource(R.color.yellow),
                           fontFamily = regular,
                           fontWeight = FontWeight.Normal,
                           fontSize = 12.sp
                        )
                    )
                }

        }
        Spacer(Modifier.height(8.dp))
        //divider
        HorizontalDivider(
            color = Color.DarkGray,
            thickness = 1.dp
        )


    }

}