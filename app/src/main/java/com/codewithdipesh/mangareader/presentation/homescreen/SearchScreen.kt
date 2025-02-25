package com.codewithdipesh.mangareader.presentation.homescreen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowOverflow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.codewithdipesh.mangareader.R
import com.codewithdipesh.mangareader.presentation.elements.TinyCard
import com.codewithdipesh.mangareader.presentation.elements.MangaCard
import com.codewithdipesh.mangareader.presentation.elements.SearchBar
import com.codewithdipesh.mangareader.presentation.navigation.Screen
import com.codewithdipesh.mangareader.ui.theme.regular
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewmodel: HomeViewmodel,
    navController: NavController
) {

    val state by viewmodel.state.collectAsState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val keyboard = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
       focusRequester.requestFocus()
    }
    LaunchedEffect(Unit) {
        viewmodel.uiEvent.collect { message ->
            Log.e("event", message)
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier=modifier
            .fillMaxSize()
            .background(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color.Black.copy(alpha = 1f),
                    Color.Black.copy(alpha = 0.8f),
                    Color.Black.copy(alpha = 0.5f),
                    Color.Black.copy(alpha = 0.3f),
                    Color.Black.copy(alpha = 0.2f),
                    Color.Black.copy(alpha = 0.1f),
                )
            )

        )
            .padding(top = 50.dp, bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        Row(
            modifier = Modifier.fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(color = colorResource(R.color.medium_gray))
                    .clickable {
                        keyboard?.hide()
                        navController.navigateUp()
                        viewmodel.clearSearchValue()
                        viewmodel.clearResultValue()
                    },
                contentAlignment = Alignment.Center
            ){
                Icon(
                    painter = painterResource(R.drawable.close_icon),
                    contentDescription = "close search bar",
                    tint = Color.White
                )
            }
            SearchBar(
                    value = state.searchValue,
                    onValueChange = {
                        viewmodel.onChangeSearchValue(it)
                        if(state.searchResult.isNotEmpty()){
                            viewmodel.clearResultValue()
                        }
                    },
                    onSearch = {
                        scope.launch(Dispatchers.IO){
                            viewmodel.searchManga()
                        }
                        keyboard?.hide()
                    },
                    placeholderText = "Search Mangas",
                    showSearchIcon = false,
                    focusRequester = focusRequester,
                    enabled = true,
                    modifier = Modifier.weight(1f)
                )

        }
        Spacer(Modifier.height(16.dp))
        //history
        if(state.searchResult.isEmpty() && state.history.isNotEmpty() && !state.isloading){
            Log.d("search","got the history cards ${state.history}")
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = stringResource(R.string.history),
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 20.sp,
                        fontFamily = regular
                    )
                )

                Icon(
                    painter = painterResource(R.drawable.history_icon),
                    contentDescription = "history",
                    tint = Color.White
                )

            }

            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                state.history.forEach {
                    TinyCard(
                        text = it,
                        onClick = {
                            viewmodel.onChangeSearchValue(it)
                            viewmodel.clearResultValue()
                            keyboard?.hide()
                            scope.launch(Dispatchers.IO) {
                                viewmodel.searchManga()
                            }
                        }
                    )
                }
            }

        }
        //results
        if(state.isloading){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator(
                    color = colorResource(R.color.yellow),
                    strokeWidth = 2.dp,
                    modifier = Modifier
                        .padding(80.dp)
                        .size(60.dp)
                )
            }
        }
        if(state.searchResult.isNotEmpty()){
            FlowRow (
                modifier =Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .verticalScroll(scrollState),
                maxItemsInEachRow = 4,
                overflow = FlowRowOverflow.Clip,
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ){
                state.searchResult.forEach {
                    MangaCard(
                        manga = it,
                        modifier = Modifier.padding(4.dp),
                        onClick = {
                            keyboard?.hide()
                            navController.navigate(Screen.Detail.createRoute(it))
                        }
                    )
                }
            }
        }


    }

}