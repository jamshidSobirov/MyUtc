package com.jmdev.myutc.domain.repository

import androidx.paging.PagingData
import com.jmdev.myutc.data.model.Character
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
//    suspend fun loadNextCharacters(): List<Character>
//    fun resetPagination()
    suspend fun loadCharacters(page: Int): List<Character>
}