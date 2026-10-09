package com.example.pokemonlist.data.repository

import com.example.pokemonlist.data.remote.PokemonApi
import com.example.pokemonlist.data.remote.toDomain
import com.example.pokemonlist.di.IoDispatcher
import com.example.pokemonlist.domain.model.PokemonPage
import com.example.pokemonlist.domain.repository.PokemonRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(
    private val api: PokemonApi,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : PokemonRepository {

    private val cache = ConcurrentHashMap<String, PokemonPage>()

    override suspend fun getPokemonPage(url: String?): PokemonPage = withContext(dispatcher) {
        val pageUrl = url ?: PokemonApi.FIRST_PAGE_URL
        cache[pageUrl] ?: api.getPokemon(pageUrl).toDomain().also { cache[pageUrl] = it }
    }
}
