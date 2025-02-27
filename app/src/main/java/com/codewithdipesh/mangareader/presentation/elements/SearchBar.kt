package com.codewithdipesh.mangareader.presentation.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codewithdipesh.mangareader.R
import com.codewithdipesh.mangareader.ui.theme.regular

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit = {},
    onMaxReachedLengthValue:()->Unit ={},
    value: String = "",
    enabled :Boolean,
    onClick:()->Unit={},
    onSearch :()->Unit = {},
    placeholderText : String = stringResource(R.string.search),
    showSearchIcon : Boolean = true,
    focusRequester: FocusRequester = FocusRequester.Default
) {
    Box(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
            .height(50.dp)
            .background(color = colorResource(R.color.medium_gray))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ){
        if(value == ""){
            Text(
                text = placeholderText,
                style = TextStyle(
                    color = Color.Gray,
                    fontSize = 16.sp,
                    fontFamily = regular
                ),
                modifier = Modifier.align(Alignment.CenterStart)
                    .then(
                        if(showSearchIcon) Modifier.padding(start = 50.dp)
                        else Modifier.padding(start = 16.dp)
                    )

            )
        }
        Row(
            modifier = Modifier.fillMaxSize()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            if(showSearchIcon){
                Icon(
                    painter = painterResource(R.drawable.search_icon),
                    contentDescription = "search",
                    tint = Color.White
                )
            }
            Spacer(Modifier.width(8.dp))
            BasicTextField(
                value = value,
                onValueChange = {
                    if(it.length <= 20){
                        onValueChange(it)
                    }else{
                        onMaxReachedLengthValue()
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester),
                textStyle = TextStyle(
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontFamily = regular
                ),
                cursorBrush = SolidColor(colorResource(R.color.yellow)),
                maxLines = 1,
                enabled = enabled,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                   onSearch = {
                       onSearch()
                   }
                )
            )

        }

    }
}