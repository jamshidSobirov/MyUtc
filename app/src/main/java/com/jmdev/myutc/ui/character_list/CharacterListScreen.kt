package com.jmdev.myutc.ui.character_list

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberAsyncImagePainter
import com.jmdev.myutc.model.Character
import com.jmdev.myutc.ui.items

@Composable
fun CharacterListScreen(viewModel: CharacterListViewModel) {
    val characters = viewModel.characterPagingFlow.collectAsLazyPagingItems()

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(characters) { character ->
            character?.let {
                CharacterItem(character = it)
            }
        }
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
            text = character.name,
            style = MaterialTheme.typography.headlineSmall
        )
    }
}
