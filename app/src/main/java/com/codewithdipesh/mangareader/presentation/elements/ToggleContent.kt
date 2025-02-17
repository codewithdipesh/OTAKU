package com.codewithdipesh.mangareader.presentation.elements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codewithdipesh.mangareader.R
import com.codewithdipesh.mangareader.presentation.mangaDetails.MangaDetailUi

@Composable
fun ToggleContent(
    modifier: Modifier = Modifier,
    list : List<MangaContent>,
    state : MangaDetailUi,
    onClick : (MangaContent) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth()
            .wrapContentHeight()
    ){
        //for full width default divider
        HorizontalDivider(
            thickness = 3.dp,
            modifier = Modifier.offset(
                y = 22.dp
            ),
            color = colorResource(R.color.medium_gray)
        )
        //option select row
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(26.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            list.forEach {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            onClick(it)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = it.name,
                            style = TextStyle(
                                color = if(state.selectedContent == it) Color.White
                                else Color.LightGray,
                                fontWeight = FontWeight.Normal,
                                fontSize = 18.sp
                            )
                        )
                        Spacer(Modifier.height(8.dp))
                        //underline
                        HorizontalDivider(
                            thickness = 3.dp,
                            color = if (state.selectedContent == it) colorResource(R.color.yellow)
                            else colorResource(R.color.medium_gray)
                        )
                    }

                }
            }
        }
    }


}