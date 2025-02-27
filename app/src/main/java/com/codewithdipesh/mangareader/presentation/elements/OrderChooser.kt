package com.codewithdipesh.mangareader.presentation.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.codewithdipesh.mangareader.R

@Composable
fun OrderChooser(
    modifier: Modifier = Modifier,
    orderType : String,
    onToggle :()->Unit,
) {
    Box(modifier = Modifier.wrapContentSize()
        .background(color = colorResource(R.color.medium_dark_gray))
        .clickable {
            onToggle()
        }
    ){
        Row(modifier = Modifier.wrapContentSize()
            .padding(4.dp)
        ){
            Box(modifier = Modifier.wrapContentSize()
                .background(
                    if(orderType == "asc") colorResource(R.color.medium_gray)
                    else colorResource(R.color.medium_dark_gray)
                )){
                Icon(
                    painter = painterResource(R.drawable.downwards),
                    contentDescription = "sort By asc",
                    tint = if(orderType == "asc") Color.White else colorResource(R.color.medium_light_gray),
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                )
            }
            Box(modifier = Modifier.wrapContentSize()
                .background(
                    if(orderType == "desc") colorResource(R.color.medium_gray)
                    else colorResource(R.color.medium_dark_gray)
                )
            ){
                Icon(
                    painter = painterResource(R.drawable.upwards),
                    contentDescription = "sort By desc",
                    tint = if(orderType == "desc") Color.White else colorResource(R.color.medium_light_gray),
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                )
            }
        }
    }
}