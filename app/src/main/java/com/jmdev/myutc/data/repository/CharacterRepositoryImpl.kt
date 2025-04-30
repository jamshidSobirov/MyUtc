package com.jmdev.myutc.data.repository

import android.util.Log
import com.jmdev.myutc.data.api.CharacterApi
import com.jmdev.myutc.domain.repository.CharacterRepository
import com.jmdev.myutc.data.model.Character

class CharacterRepositoryImpl(
    private val api: CharacterApi
) : CharacterRepository {

    override suspend fun loadCharacters(page: Int): PageState<List<Character>> {
        return try {
            Log.d("@@@@", "loadCharacters: $page")
            val response = api.getCharacters(page)
            PageState.Success(response.results)
        } catch (e: Exception) {
            PageState.Error(e) // Handle exceptions and return an error state
        }
    }
}
