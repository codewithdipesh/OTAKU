package com.codewithdipesh.mangareader.presentation.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codewithdipesh.mangareader.R
import com.codewithdipesh.mangareader.domain.model.ReadMode
import com.codewithdipesh.mangareader.ui.theme.regular

@Composable
fun ReadModeChooser(
    modifier: Modifier = Modifier,
    readMode : ReadMode,
    onToggle :()->Unit,
){
    Box(
        modifier = Modifier.wrapContentSize()
            .clickable {
                onToggle()
            }
    ){
        Text(
            text = if(readMode == ReadMode.Horizontal) "Horizontal" else "Vertical",
            style = TextStyle(
                fontFamily = regular,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = colorResource(R.color.yellow)
            )
        )
    }
}