package com.example.pokemonlist.domain.repository

import com.example.pokemonlist.domain.model.PokemonPage

interface PokemonRepository {
    suspend fun getPokemonPage(url: String?): PokemonPage
}
