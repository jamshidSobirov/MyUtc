package com.jmdev.myutc.data.api

import com.jmdev.myutc.data.model.CharacterResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CharacterApi {
    @GET("character")
    suspend fun getCharacters(
        @Query("page") page: Int
    ): CharacterResponse
}
