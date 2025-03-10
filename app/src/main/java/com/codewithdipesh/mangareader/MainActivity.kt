 package com.codewithdipesh.mangareader

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.codewithdipesh.mangareader.presentation.downloads.DownloadsViewModel
import com.codewithdipesh.mangareader.presentation.homescreen.HomeScreen
import com.codewithdipesh.mangareader.presentation.homescreen.HomeViewmodel
import com.codewithdipesh.mangareader.presentation.mangaDetails.MangaDetailsViewModel
import com.codewithdipesh.mangareader.presentation.navigation.MangaNavHost
import com.codewithdipesh.mangareader.presentation.reader.ReaderViewModel
import com.codewithdipesh.mangareader.ui.theme.MangaReaderTheme
import dagger.hilt.android.AndroidEntryPoint

 @AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val viewModel by viewModels<HomeViewmodel>()
        val mangaViewModel by viewModels<MangaDetailsViewModel>()
        val readerViewModel by viewModels<ReaderViewModel>()
        val downloadViewModel by viewModels<DownloadsViewModel>()
        setContent {
            MangaReaderTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(
                        homeViewModel = viewModel,
                        mangaViewModel = mangaViewModel,
                        readerViewModel = readerViewModel,
                        downloadViewModel = downloadViewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MangaReaderTheme {
        Greeting("Android")
    }
}