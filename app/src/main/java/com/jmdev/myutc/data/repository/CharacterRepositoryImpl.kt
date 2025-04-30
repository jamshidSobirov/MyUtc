package com.jmdev.myutc.data.repository

import android.util.Log
import com.jmdev.myutc.data.api.CharacterApi
import com.jmdev.myutc.domain.repository.CharacterRepository
import com.jmdev.myutc.data.model.Character

class CharacterRepositoryImpl(
    private val api: CharacterApi
) : CharacterRepository {

    override suspend fun loadCharacters(page: Int): List<Character> {
        Log.d("@@@@", "loadCharacters: $page")
        return api.getCharacters(page).results
    }
}
