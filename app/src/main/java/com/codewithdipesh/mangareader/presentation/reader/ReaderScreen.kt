package com.codewithdipesh.mangareader.presentation.reader

import android.Manifest
import android.app.Activity
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.ImageLoader
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.codewithdipesh.mangareader.R
import com.codewithdipesh.mangareader.domain.model.ReadMode
import com.codewithdipesh.mangareader.presentation.downloads.DownloadsViewModel
import com.codewithdipesh.mangareader.presentation.elements.LoaderWithQuotes
import com.codewithdipesh.mangareader.presentation.elements.ReadModeChooser
import com.codewithdipesh.mangareader.presentation.elements.SlidingButton
import com.codewithdipesh.mangareader.presentation.elements.resetToSystemBrightness
import com.codewithdipesh.mangareader.presentation.elements.setScreenBrightness
import com.codewithdipesh.mangareader.presentation.mangaDetails.MangaDetailsViewModel
import com.codewithdipesh.mangareader.ui.theme.regular
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.internal.wait
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderScreen(
    modifier: Modifier = Modifier,
    chapterId:String,
    viewModel: ReaderViewModel,
    detailsViewModel: MangaDetailsViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val imageLoader = remember { ImageLoader.Builder(context).build() }
    val state by viewModel.uiState.collectAsState()
    val detailState by detailsViewModel.state.collectAsState()
    val scrollState = rememberScrollState()
    var showBottomSheet by remember { mutableStateOf(false) }
    var isImageLoading by remember { mutableStateOf(true)}
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )

    var brightness by remember { mutableStateOf(0.8f) }
    val activity = context as? Activity


    BackHandler {
        //back navigate
        scope.launch {
            navController.navigateUp()
            viewModel.clearUi()
            resetToSystemBrightness(activity)
        }
    }

    LaunchedEffect(Unit){
        scope.launch {
            if(chapterId.isNotEmpty()){
                Log.d("reader","load chapter")
                viewModel.load(chapterId,imageLoader,context)
                delay(10000)//fi\\\
                viewModel.stopLoading()
                Log.d("reader","no loader anymore..")
            }
        }
    }
    //consuming detailViewModel state for internet
    LaunchedEffect(detailState.isInternetAvailable){
        snapshotFlow { detailState.isInternetAvailable }
            .collectLatest { isAvailable->
                if(!isAvailable){
                    Toast.makeText(context,"No Internet",Toast.LENGTH_SHORT).show()
                }else{
                    viewModel.preloadPages(imageLoader,context)
                }
            }
    }

    LaunchedEffect(state.currentPage , state.manualTrigger){
      if(state.currentPage == state.pageSize -1 ){ //last page ->mark for visited
          state.chapter?.let { detailsViewModel.markForVisitedChapter(state.chapter!!) }
      }
        //load future pages
      viewModel.preloadPages(imageLoader,context)
    }
    val currentPageLink = remember(state.currentPage){ viewModel.getPageLink(state.currentPage)}
    val painter = if (!currentPageLink.isNullOrEmpty()) {
        rememberAsyncImagePainter(
            model = ImageRequest.Builder(context)
                .data(currentPageLink)
                .build(),
            imageLoader = imageLoader,
            onState = { painterState ->
                when (painterState) {
                    is AsyncImagePainter.State.Loading -> isImageLoading = true
                    is AsyncImagePainter.State.Success -> isImageLoading = false
                    is AsyncImagePainter.State.Error -> isImageLoading = false
                    is AsyncImagePainter.State.Empty -> isImageLoading = true
                }
            }
        )
    } else {
        null
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(color = colorResource(R.color.dark_gray)),
    ){
        if(!state.isPreloadComplete){
            LoaderWithQuotes()
        }else{
            //page reader screen
            Log.d("reader","reading")
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                //upper row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 50.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    //close button
                    Box(
                        Modifier
                            .size(40.dp) // Fixed size
                            .fillMaxHeight()
                            .background(color = colorResource(R.color.medium_gray))
                            .clickable {
                                //back navigate
                                scope.launch {
                                    navController.navigateUp()
                                    viewModel.clearUi()
                                    resetToSystemBrightness(activity)
                                }
                            },
                        contentAlignment = Alignment.Center
                    ){
                        Icon(
                            painter = painterResource(R.drawable.close_icon),
                            contentDescription = "close",
                            tint = Color.White
                        )
                    }

                    //Chapter and page
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .weight(0.7f)

                    ) {
                        if(state.chapter != null){
                            Text(
                                text = "${state.chapter!!.title}",
                                style = TextStyle(
                                    color = colorResource(R.color.yellow),
                                    fontWeight = FontWeight.Normal,
                                    fontFamily = regular,
                                    fontSize = 12.sp
                                ),
                                textAlign = TextAlign.Center,
                            )
                        }
                        if(state.readMode == ReadMode.Horizontal){
                            Text(
                                text = "${state.currentPage}/${state.pageSize}",
                                style = TextStyle(
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = regular,
                                    fontSize = 18.sp
                                )
                            )
                        }
                    }

                    //download and setting button
                    Row(modifier = Modifier.wrapContentSize(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ){
                        //downloads button
                        Box(
                            Modifier
                                .size(40.dp) // Fixed size
                                .fillMaxHeight()
                                .background(color = colorResource(R.color.medium_gray).copy(0.6f))
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = rememberRipple(
                                        color = colorResource(R.color.yellow).copy(0.5f),
                                        radius = 40.dp
                                    )
                                ){
                                    //todo download
                                   if(!state.isDownloaded && !state.isDownloading){
                                       viewModel.startDownloadChapter(
                                           context,
                                           state.chapter!!,
                                           detailState.title,
                                           state.lowQualityImageList,
                                           detailState.coverImage!!
                                       )
                                   }else{
                                       scope.launch {
                                           Toast.makeText(context,"Check Download Section",Toast.LENGTH_SHORT).show()
                                       }
                                   }
                                },
                            contentAlignment = Alignment.Center,
                            ){
                            Icon(
                                painter = painterResource(R.drawable.downloads_icon),
                                contentDescription = "download",
                                tint = if(state.isDownloaded) Color.LightGray else Color.White
                            )
                        }
                        //settings button
                        Box(
                            Modifier
                                .size(40.dp) // Fixed size
                                .fillMaxHeight()
                                .background(color = colorResource(R.color.medium_gray))
                                .clickable {
                                    showBottomSheet = true
                                },
                            contentAlignment = Alignment.Center,
                            ){
                            Icon(
                                painter = painterResource(R.drawable.setting_icon),
                                contentDescription = "close",
                                tint = Color.White
                            )
                        }
                    }

                }
                Spacer(Modifier.height(30.dp))
                //Image
                //horizontal changing mode
                if(state.readMode == ReadMode.Horizontal){
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .verticalScroll(scrollState),
                        contentAlignment = Alignment.TopCenter
                    ){
                        if (!currentPageLink.isNullOrEmpty() && painter != null) {
                            Image(
                                painter = painter,
                                contentDescription = "Page ${state.currentPage}",
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentScale = ContentScale.FillWidth,
                                colorFilter = ColorFilter.colorMatrix(
                                    colorMatrix = ColorMatrix().apply {
                                        setToScale(brightness,brightness,brightness,1f)
                                    }
                                )
                            )
                        }
                        if(state.isLoading || isImageLoading ){
                            CircularProgressIndicator(
                                color = colorResource(R.color.yellow),
                                strokeWidth = 2.dp,
                                modifier = Modifier
                                    .size(50.dp)
                                    .align(Alignment.Center)
                            )
                        }
                    }
                }
                //vertical scrolling mode
                else{
                    val lazylistState = rememberLazyListState()

                    LaunchedEffect(lazylistState) {
                        snapshotFlow {
                            if(lazylistState.layoutInfo.visibleItemsInfo.isNotEmpty()){
                                lazylistState.layoutInfo.visibleItemsInfo.first().index
                            } else null
                        }
                        .collectLatest { visibleIndex->
                           visibleIndex?.let {
                               if(it + 1 != state.currentPage){
                                   viewModel.increasePage()
                               }
                           }
                        }
                    }

                    LazyColumn(
                        state = lazylistState,
                        modifier = Modifier.fillMaxSize()
                    ){
                        items(state.pageSize){index->
                            val pageNumber = index + 1
                            val pageLink = remember { viewModel.getPageLink(pageNumber)}
                            val pagePainter = if (!pageLink.isNullOrEmpty()) {
                                rememberAsyncImagePainter(
                                    model = ImageRequest.Builder(context)
                                        .data(pageLink)
                                        .build(),
                                    imageLoader = imageLoader,
                                    onState = { painterState ->
                                        when (painterState) {
                                            is AsyncImagePainter.State.Loading -> isImageLoading = true
                                            is AsyncImagePainter.State.Success -> isImageLoading = false
                                            is AsyncImagePainter.State.Error -> isImageLoading = false
                                            is AsyncImagePainter.State.Empty -> isImageLoading = true
                                        }
                                    }
                                )
                            } else {
                                null
                            }

                            Box(modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                                contentAlignment = Alignment.Center
                            ){
                                if (!pageLink.isNullOrEmpty() && pagePainter != null) {
                                    Image(
                                        painter = pagePainter,
                                        contentDescription = "Page ${state.currentPage}",
                                        modifier = Modifier
                                            .fillMaxSize(),
                                        contentScale = ContentScale.FillWidth,
                                        colorFilter = ColorFilter.colorMatrix(
                                            colorMatrix = ColorMatrix().apply {
                                                setToScale(brightness,brightness,brightness,1f)
                                            }
                                        )
                                    )
                                }
                                if(state.isLoading || isImageLoading ){
                                    CircularProgressIndicator(
                                        color = colorResource(R.color.yellow),
                                        strokeWidth = 2.dp,
                                        modifier = Modifier
                                            .size(50.dp)
                                            .align(Alignment.Center)
                                    )
                                }
                            }


                        }
                    }
                }

            }

            //page changer
            if(state.readMode == ReadMode.Horizontal){
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(horizontal = 16.dp, vertical = 60.dp),
                    horizontalArrangement =
                    if(state.currentPage == 1)
                        Arrangement.End
                    else if(state.currentPage == state.pageSize)
                        Arrangement.Start
                    else Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if(state.currentPage >1){
                        Box(
                            Modifier
                                .size(50.dp)
                                .background(color = Color.Black.copy(alpha = 0.7f))
                                .clickable {
                                    viewModel.decreasePage()
                                },
                            contentAlignment = Alignment.Center
                        ){
                            Icon(
                                painter = painterResource(R.drawable.back_nav_icon),
                                contentDescription = "previous page",
                                tint = Color.White
                            )
                        }
                    }
                    if(state.currentPage < state.pageSize){
                        Box(
                            Modifier
                                .size(50.dp)
                                .background(color = Color.Black.copy(alpha = 0.7f))
                                .clickable {
                                    viewModel.increasePage()
//                            //reset zoom and panning
//                            scale =1f
//                            offsetX=0f
//                            offsetY=0f
                                },
                            contentAlignment = Alignment.Center
                        ){
                            Icon(
                                painter = painterResource(R.drawable.show_more_icon),
                                contentDescription = "next page",
                                tint = Color.White
                            )
                        }
                    }
                }
            }
            //bottomSheet
            if(showBottomSheet){
                ModalBottomSheet(
                    onDismissRequest = {showBottomSheet = false},
                    sheetState = sheetState,
                    modifier = Modifier.wrapContentHeight(),
                    containerColor = colorResource(R.color.dark_gray)
                ){
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ){
                        //ReadMode
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Text(
                                text = "Read mode",
                                style = TextStyle(
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontFamily = regular
                                )
                            )
                            ReadModeChooser(
                               readMode = state.readMode,
                               onToggle = { viewModel.toggleReadMode() }
                            )
                        }

                        //high quality
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Text(
                                text = "Original Quality",
                                style = TextStyle(
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontFamily = regular
                                )
                            )
                            SlidingButton(
                                isSelected = state.isHighQuality,
                                onToggle = {
                                    viewModel.toggleHighQuality()
                                }
                            )
                        }

                        //brightness
                        Text(
                            text = "Brightness",
                            style = TextStyle(
                                color = Color.White,
                                fontSize = 18.sp,
                                fontFamily = regular
                            ),
                            modifier = Modifier
                                .align(Alignment.Start)
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Icon(
                                painter = painterResource(R.drawable.low_brightness_icon),
                                contentDescription = "low brightness",
                                tint = colorResource(R.color.medium_light_gray),
                                modifier = Modifier.weight(0.2f)
                            )
                            Slider(
                                value = brightness,
                                onValueChange = {
                                    brightness = it
                                    setScreenBrightness(brightness,activity)
                                },
                                valueRange = 0.3f..1f,
                                colors = SliderDefaults.colors(
                                    activeTrackColor = colorResource(R.color.yellow),
                                    disabledActiveTrackColor = colorResource(R.color.medium_gray),
                                    thumbColor = Color.White
                                ),
                                modifier = Modifier
                                    .weight(0.8f)
                            )
                            Icon(
                                painter = painterResource(R.drawable.high_brightness_icon),
                                contentDescription = "high brightness",
                                tint = colorResource(R.color.deep_yellow),
                                modifier = Modifier.weight(0.2f)
                            )
                        }

                    }
                }
            }
        }

    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderScreen(
    modifier: Modifier = Modifier,
    downloadedChapterId:String,
    viewModel: DownloadsViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val state by viewModel.chapterState.collectAsState()
    val scrollState = rememberScrollState()
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )

    var brightness by remember { mutableStateOf(0.8f) }
    val activity = context as? Activity

    LaunchedEffect(Unit){
        viewModel.getDownloadedChapter(downloadedChapterId)
    }

    val imageFile = remember(state.currentPage) {
        if (state.pages.isNotEmpty() && state.currentPage - 1 in state.pages.indices) {
            File(state.pages[state.currentPage - 1])
        } else {
            null
        }
    }

    val painter = if(imageFile != null){
        rememberAsyncImagePainter(
            model = imageFile?.let {
                ImageRequest.Builder(context).data(it).crossfade(true).build()
            }
        )
    }else{
        null
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(color = colorResource(R.color.dark_gray)),
    ){
        if(state.isLoading){
            LoaderWithQuotes()
        }else{
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                //upper row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 50.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    //close button
                    Box(
                        Modifier
                            .size(40.dp) // Fixed size
                            .fillMaxHeight()
                            .background(color = colorResource(R.color.medium_gray))
                            .clickable {
                                //back navigate
                                scope.launch {
                                    navController.navigateUp()
                                    viewModel.clearChapterUi()
                                    resetToSystemBrightness(activity)
                                }
                            },
                        contentAlignment = Alignment.Center
                    ){
                        Icon(
                            painter = painterResource(R.drawable.close_icon),
                            contentDescription = "close",
                            tint = Color.White
                        )
                    }

                    //Chapter and page
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .weight(0.7f)

                    ) {
                        Text(
                            text = state.chapterName,
                            style = TextStyle(
                                color = colorResource(R.color.yellow),
                                fontWeight = FontWeight.Normal,
                                fontFamily = regular,
                                fontSize = 12.sp
                            ),
                            textAlign = TextAlign.Center,
                        )
                        if(state.readMode == ReadMode.Horizontal){
                            Text(
                                text = "${state.currentPage}/${state.pageSize}",
                                style = TextStyle(
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = regular,
                                    fontSize = 18.sp
                                )
                            )
                        }
                    }

                    //settings button
                    Box(
                        Modifier
                            .size(40.dp) // Fixed size
                            .fillMaxHeight()
                            .background(color = colorResource(R.color.medium_gray))
                            .clickable {
                                showBottomSheet = true
                            },
                        contentAlignment = Alignment.Center,
                    ){
                        Icon(
                            painter = painterResource(R.drawable.setting_icon),
                            contentDescription = "close",
                            tint = Color.White
                        )
                    }

                }
                Spacer(Modifier.height(30.dp))
                //Image
                //horizontal changing mode
                if(state.readMode == ReadMode.Horizontal){
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .verticalScroll(scrollState),
                        contentAlignment = Alignment.TopCenter
                    ){
                        if(painter != null){
                            Image(
                                painter = painter,
                                contentDescription = "Page ${state.currentPage}",
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentScale = ContentScale.FillWidth,
                                colorFilter = ColorFilter.colorMatrix(
                                    colorMatrix = ColorMatrix().apply {
                                        setToScale(brightness,brightness,brightness,1f)
                                    }
                                )
                            )
                        }

                    }
                }
                //vertical scrolling mode
                else{
                    val lazylistState = rememberLazyListState()

                    LaunchedEffect(lazylistState) {
                        snapshotFlow {
                            if(lazylistState.layoutInfo.visibleItemsInfo.isNotEmpty()){
                                lazylistState.layoutInfo.visibleItemsInfo.first().index
                            } else null
                        }
                            .collectLatest { visibleIndex->
                                visibleIndex?.let {
                                    if(it + 1 != state.currentPage){
                                        viewModel.increasePage()
                                    }
                                }
                            }
                    }

                    LazyColumn(
                        state = lazylistState,
                        modifier = Modifier.fillMaxSize()
                    ){
                        items(state.pageSize){index->
                            val pageFile = if (state.pages.isNotEmpty()) {
                                File(state.pages[index])
                            } else {
                                null
                            }
                            val pagePainter = if(pageFile != null){
                                rememberAsyncImagePainter(
                                    model = ImageRequest.Builder(context)
                                        .data(pageFile)
                                        .build()
                                )
                            }else{
                                null
                            }

                            Box(modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                                contentAlignment = Alignment.Center
                            ){
                                if(pagePainter != null){
                                    Image(
                                        painter = pagePainter,
                                        contentDescription = "Page ${state.currentPage}",
                                        modifier = Modifier
                                            .fillMaxSize(),
                                        contentScale = ContentScale.FillWidth,
                                        colorFilter = ColorFilter.colorMatrix(
                                            colorMatrix = ColorMatrix().apply {
                                                setToScale(brightness,brightness,brightness,1f)
                                            }
                                        )
                                    )
                                }
                            }
                        }
                    }
                }

            }

            //page changer
            if(state.readMode == ReadMode.Horizontal){
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(horizontal = 16.dp, vertical = 60.dp),
                    horizontalArrangement =
                    if(state.currentPage == 1)
                        Arrangement.End
                    else if(state.currentPage == state.pageSize)
                        Arrangement.Start
                    else Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if(state.currentPage >1){
                        Box(
                            Modifier
                                .size(50.dp)
                                .background(color = Color.Black.copy(alpha = 0.7f))
                                .clickable {
                                    viewModel.decreasePage()
                                },
                            contentAlignment = Alignment.Center
                        ){
                            Icon(
                                painter = painterResource(R.drawable.back_nav_icon),
                                contentDescription = "previous page",
                                tint = Color.White
                            )
                        }
                    }
                    if(state.currentPage < state.pageSize){
                        Box(
                            Modifier
                                .size(50.dp)
                                .background(color = Color.Black.copy(alpha = 0.7f))
                                .clickable {
                                    viewModel.increasePage()
                                },
                            contentAlignment = Alignment.Center
                        ){
                            Icon(
                                painter = painterResource(R.drawable.show_more_icon),
                                contentDescription = "next page",
                                tint = Color.White
                            )
                        }
                    }
                }
            }
            //bottomSheet
            if(showBottomSheet){
                ModalBottomSheet(
                    onDismissRequest = {showBottomSheet = false},
                    sheetState = sheetState,
                    modifier = Modifier.wrapContentHeight(),
                    containerColor = colorResource(R.color.dark_gray)
                ){
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ){
                        //ReadMode
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Text(
                                text = "Read mode",
                                style = TextStyle(
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontFamily = regular
                                )
                            )
                            ReadModeChooser(
                                readMode = state.readMode,
                                onToggle = { viewModel.toggleReadMode() }
                            )
                        }
                        //brightness
                        Text(
                            text = "Brightness",
                            style = TextStyle(
                                color = Color.White,
                                fontSize = 18.sp,
                                fontFamily = regular
                            ),
                            modifier = Modifier
                                .align(Alignment.Start)
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Icon(
                                painter = painterResource(R.drawable.low_brightness_icon),
                                contentDescription = "low brightness",
                                tint = colorResource(R.color.medium_light_gray),
                                modifier = Modifier.weight(0.2f)
                            )
                            Slider(
                                value = brightness,
                                onValueChange = {
                                    brightness = it
                                    setScreenBrightness(brightness,activity)
                                },
                                valueRange = 0.3f..1f,
                                colors = SliderDefaults.colors(
                                    activeTrackColor = colorResource(R.color.yellow),
                                    disabledActiveTrackColor = colorResource(R.color.medium_gray),
                                    thumbColor = Color.White
                                ),
                                modifier = Modifier
                                    .weight(0.8f)
                            )
                            Icon(
                                painter = painterResource(R.drawable.high_brightness_icon),
                                contentDescription = "high brightness",
                                tint = colorResource(R.color.deep_yellow),
                                modifier = Modifier.weight(0.2f)
                            )
                        }

                    }
                }
            }
        }

    }


}