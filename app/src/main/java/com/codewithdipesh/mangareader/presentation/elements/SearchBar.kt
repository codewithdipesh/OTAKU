package com.codewithdipesh.mangareader.presentation.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codewithdipesh.mangareader.R
import com.codewithdipesh.mangareader.ui.theme.regular

@Composable
fun SearchBar(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .height(50.dp)
            .background(color = colorResource(R.color.medium_gray)),
        contentAlignment = Alignment.Center
    ){
        Row(
            modifier = Modifier.fillMaxSize()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                painter = painterResource(R.drawable.search_icon),
                contentDescription = "search"
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.search),
                style = TextStyle(
                    color = Color.LightGray,
                    fontSize = 16.sp,
                    fontFamily = regular
                )
            )
        }
    }
}