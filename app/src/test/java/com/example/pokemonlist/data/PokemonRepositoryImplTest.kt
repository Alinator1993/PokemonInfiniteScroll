package com.example.pokemonlist.data

import com.example.pokemonlist.data.remote.PokemonApi
import com.example.pokemonlist.data.remote.PokemonResponseDto
import com.example.pokemonlist.data.remote.PokemonResultDto
import com.example.pokemonlist.data.repository.PokemonRepositoryImpl
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class PokemonRepositoryImplTest {

    private class FakePokemonApi : PokemonApi {
        val requestedUrls = mutableListOf<String>()

        override suspend fun getPokemon(url: String): PokemonResponseDto {
            requestedUrls.add(url)
            return PokemonResponseDto(
                count = 1302,
                next = "next-page",
                results = listOf(PokemonResultDto("bulbasaur", "https://pokeapi.co/api/v2/pokemon/1/"))
            )
        }
    }

    private val api = FakePokemonApi()

    @Test
    fun `null url loads first page and maps to domain`() = runTest {
        val repository = PokemonRepositoryImpl(api, StandardTestDispatcher(testScheduler))

        val page = repository.getPokemonPage(null)

        assertEquals(listOf(PokemonApi.FIRST_PAGE_URL), api.requestedUrls)
        assertEquals("bulbasaur", page.pokemon.first().name)
        assertEquals("next-page", page.nextUrl)
    }

    @Test
    fun `returns cached page instead of calling api again`() = runTest {
        val repository = PokemonRepositoryImpl(api, StandardTestDispatcher(testScheduler))

        repository.getPokemonPage("next-page")
        repository.getPokemonPage("next-page")

        assertEquals(1, api.requestedUrls.size)
    }
}
