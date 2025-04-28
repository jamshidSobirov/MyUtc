package com.jmdev.myutc.model

data class CharacterResponse(
    val info: Info,
    val results: List<Character>
)

data class Info(
    val next: String?
)

data class Character(
    val id: Int,
    val name: String,
    val image: String
)
