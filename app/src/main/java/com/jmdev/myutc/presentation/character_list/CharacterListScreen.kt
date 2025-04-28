package com.jmdev.myutc.presentation.character_list

import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberAsyncImagePainter
import com.jmdev.myutc.data.model.Character
import com.jmdev.myutc.presentation.extensions.items
import com.jmdev.myutc.receivers.NetworkBroadcastReceiver
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterListScreen(viewModel: CharacterListViewModel) {
    val context = LocalContext.current

    val characters = viewModel.characterPagingFlow.collectAsLazyPagingItems()
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

        val isRefreshing =
            remember { derivedStateOf { characters.loadState.refresh is androidx.paging.LoadState.Loading } }

        PullToRefreshBox(
            isRefreshing = isRefreshing.value,
            onRefresh = {
                characters.refresh()
            },
            modifier = Modifier.fillMaxSize(),
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(characters) { character ->
                    character?.let {
                        CharacterItem(character = it)
                    }
                }

                characters.apply {
                    when {
                        loadState.append is LoadState.Error -> {
                            item {
                                RetryItem(message = "Failed to load more items. \nTry again.",
                                    onRetryClick = { retry() })
                            }
                        }

                        loadState.refresh is LoadState.Error -> {
                            item {
                                RetryItem(message = "Failed to load items. \nCheck your connection.",
                                    onRetryClick = { retry() })
                            }
                        }
                    }
                }
            }

            if (characters.loadState.append is LoadState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.padding(16.dp)
                )
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
fun CharacterItem(character: Character) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
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
