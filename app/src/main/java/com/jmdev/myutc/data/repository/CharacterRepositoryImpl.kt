package com.jmdev.myutc.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.jmdev.myutc.data.api.CharacterApi
import com.jmdev.myutc.data.remote.CharacterPagingSource
import com.jmdev.myutc.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import com.jmdev.myutc.data.model.Character

class CharacterRepositoryImpl(
    private val api: CharacterApi
) : CharacterRepository {
    override fun getCharacters(): Flow<PagingData<Character>> {
        return Pager(config = PagingConfig(
            pageSize = 20, enablePlaceholders = false
        ), pagingSourceFactory = { CharacterPagingSource(api) }).flow
    }
}
