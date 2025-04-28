package com.jmdev.myutc.presentation.character_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.jmdev.myutc.data.repository.CharacterRepositoryImpl
import kotlinx.coroutines.flow.Flow
import com.jmdev.myutc.data.model.Character
import com.jmdev.myutc.domain.repository.CharacterRepository

class CharacterListViewModel(
    repository: CharacterRepository
) : ViewModel() {

    val characterPagingFlow: Flow<PagingData<Character>> =
        repository.getCharacters()
            .cachedIn(viewModelScope)
}
