package com.example.pokemonlist.domain.model

data class Pokemon(
    val name: String
)

data class PokemonPage(
    val pokemon: List<Pokemon>,
    val nextUrl: String?
)
