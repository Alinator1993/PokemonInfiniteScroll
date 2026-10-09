package com.example.pokemonlist.presentation

import com.example.pokemonlist.domain.model.Pokemon
import com.example.pokemonlist.domain.model.PokemonPage
import com.example.pokemonlist.domain.repository.PokemonRepository
import com.example.pokemonlist.domain.usecase.GetPokemonPageUseCase
import com.example.pokemonlist.presentation.pokemon.PokemonViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PokemonViewModelTest {

    private class FakePokemonRepository : PokemonRepository {
        val requestedUrls = mutableListOf<String?>()
        var shouldFail = false

        override suspend fun getPokemonPage(url: String?): PokemonPage {
            requestedUrls.add(url)
            if (shouldFail) throw RuntimeException("Network error")
            return when (url) {
                null -> PokemonPage(listOf(Pokemon("bulbasaur")), nextUrl = "page2")
                else -> PokemonPage(listOf(Pokemon("ivysaur")), nextUrl = null)
            }
        }
    }

    private val repository = FakePokemonRepository()

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel() = PokemonViewModel(GetPokemonPageUseCase(repository))

    @Test
    fun `loadMore uses next url and stops at the end`() {
        val viewModel = createViewModel()

        viewModel.loadMore()
        viewModel.loadMore()

        val state = viewModel.uiState.value
        assertEquals(listOf("bulbasaur", "ivysaur"), state.pokemon.map { it.name })
        assertEquals(listOf(null, "page2"), repository.requestedUrls)
        assertTrue(state.endReached)
    }

    @Test
    fun `shows error when request fails`() {
        repository.shouldFail = true

        val viewModel = createViewModel()

        assertEquals("Network error", viewModel.uiState.value.error)
    }
}
