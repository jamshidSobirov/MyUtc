package com.jmdev.myutc.domain.repository

import androidx.paging.PagingData
import com.jmdev.myutc.data.model.Character
import com.jmdev.myutc.data.repository.PageState
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
    suspend fun loadCharacters(page: Int): PageState<List<Character>>
}