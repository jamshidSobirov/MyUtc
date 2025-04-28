package com.jmdev.myutc.data.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import com.jmdev.myutc.model.Character

class CharacterRepository(
    private val api: CharacterApi
) {
    fun getCharacters(): Flow<PagingData<Character>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { CharacterPagingSource(api) }
        ).flow
    }
}
