package com.jmdev.myutc.presentation.character_list

import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.jmdev.myutc.data.model.Character
import com.jmdev.myutc.receivers.NetworkBroadcastReceiver
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterListScreen(viewModel: CharacterListViewModel) {
    val context = LocalContext.current
    val isLoading by viewModel.isLoading.collectAsState()
    val hasError by viewModel.hasError.collectAsState()
    val characters by viewModel.characters.collectAsState()
    val listState = rememberLazyListState()

    // Local state to control RetryItem visibility
    var showRetryItem by remember { mutableStateOf(false) }

    // Show RetryItem only when there's an error
    LaunchedEffect(hasError) {
        if (hasError) {
            showRetryItem = true
        }
    }

    var isConnected by remember { mutableStateOf(true) }
    var showGreenView by remember { mutableStateOf(false) }

    val receiver = remember {
        NetworkBroadcastReceiver(onNetworkChange = { connected ->
            if (connected) {
                showGreenView = true
                isConnected = true
            } else {
                isConnected = false
            }
        })
    }

    DisposableEffect(Unit) {
        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        context.registerReceiver(receiver, intentFilter)

        onDispose {
            context.unregisterReceiver(receiver)
        }
    }

    LaunchedEffect(showGreenView) {
        if (showGreenView) {
            delay(2000)
            showGreenView = false
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        if (!isConnected) {
            StatusBox(message = "Internet is disconnected", backgroundColor = Color.Red)
        } else if (showGreenView) {
            StatusBox(message = "You are connected", backgroundColor = Color.Green)
        }


        PullToRefreshBox(
            isRefreshing = isLoading && characters.isEmpty(),
            onRefresh = {
                viewModel.resetCharacters()
            },
            modifier = Modifier.fillMaxSize(),
        ) {
            LazyColumn(
                state = listState, modifier = Modifier.fillMaxSize()
            ) {
                items(characters) { character ->
                    CharacterItem(character, characters.indexOf(character))
                }

                // Show RetryItem if there's an error
                if (showRetryItem) {
                    item {
                        RetryItem(message = "Failed to load items. Check your connection.",
                            onRetryClick = {
                                viewModel.retryLoadingPage()
                                showRetryItem = false // Hide RetryItem after retry
                            })
                    }
                }
            }

            // Detect when user scrolls to the end of the list
            LaunchedEffect(listState) {
                snapshotFlow { listState.layoutInfo }.collect { layoutInfo ->
                    val totalItems = layoutInfo.totalItemsCount
                    val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0

                    if (lastVisibleItemIndex >= totalItems - 1 && !showRetryItem) {
                        viewModel.loadNextCharacters()
                    }
                }
            }
        }
    }
}

@Composable
fun RetryItem(message: String, onRetryClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            message, modifier = Modifier
                .padding(end = 8.dp)
                .align(Alignment.CenterVertically)
        )
        Button(onClick = onRetryClick) {
            Text("Retry")
        }
    }
}

@Composable
fun StatusBox(message: String, backgroundColor: Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message, color = Color.White, textAlign = TextAlign.Center
        )
    }
}

@Composable
fun CharacterItem(character: Character, indexOf: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(text = indexOf.toString())
        Spacer(modifier = Modifier.width(8.dp))
        Image(
            painter = rememberAsyncImagePainter(character.image),
            contentDescription = character.name,
            modifier = Modifier.size(80.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = character.name, style = MaterialTheme.typography.headlineSmall
        )
    }
}
