package com.example.pokemonlist.data.remote

import com.example.pokemonlist.domain.model.Pokemon
import com.example.pokemonlist.domain.model.PokemonPage

data class PokemonResponseDto(
    val count: Int,
    val next: String?,
    val results: List<PokemonResultDto>
)

data class PokemonResultDto(
    val name: String,
    val url: String
)

fun PokemonResponseDto.toDomain(): PokemonPage {
    return PokemonPage(
        pokemon = results.map { Pokemon(name = it.name) },
        nextUrl = next
    )
}
