package com.example.pokemonlist.data.remote

import retrofit2.http.GET
import retrofit2.http.Url

interface PokemonApi {
    @GET
    suspend fun getPokemon(@Url url: String): PokemonResponseDto

    companion object {
        const val BASE_URL = "https://pokeapi.co/api/v2/"
        const val FIRST_PAGE_URL = "${BASE_URL}pokemon?limit=10&offset=0"
    }
}
