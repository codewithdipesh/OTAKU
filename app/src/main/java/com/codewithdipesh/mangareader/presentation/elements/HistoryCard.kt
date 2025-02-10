package com.codewithdipesh.mangareader.presentation.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codewithdipesh.mangareader.ui.theme.regular

@Composable
fun HistoryCard(
    modifier: Modifier = Modifier,
    historyTerm : String,
    onClick: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .padding(8.dp)
            .height(30.dp)
            .wrapContentWidth()
            .background(color = Color.DarkGray)
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = historyTerm,
            style = TextStyle(
                color = Color.White,
                fontSize = 16.sp,
                fontFamily = regular
            ),
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}