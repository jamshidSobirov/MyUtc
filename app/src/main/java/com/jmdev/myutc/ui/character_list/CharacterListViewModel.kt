package com.jmdev.myutc.ui.character_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.jmdev.myutc.data.remote.CharacterRepository
import kotlinx.coroutines.flow.Flow
import com.jmdev.myutc.model.Character

class CharacterListViewModel(
    repository: CharacterRepository
) : ViewModel() {

    val characterPagingFlow: Flow<PagingData<Character>> =
        repository.getCharacters()
            .cachedIn(viewModelScope)
}
