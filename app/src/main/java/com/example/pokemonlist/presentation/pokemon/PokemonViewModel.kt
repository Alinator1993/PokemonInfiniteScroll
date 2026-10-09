package com.example.pokemonlist.presentation.pokemon

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemonlist.domain.model.Pokemon
import com.example.pokemonlist.domain.usecase.GetPokemonPageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PokemonUiState(
    val pokemon: List<Pokemon> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val endReached: Boolean = false
)

@HiltViewModel
class PokemonViewModel @Inject constructor(
    private val getPokemonPage: GetPokemonPageUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PokemonUiState())
    val uiState: StateFlow<PokemonUiState> = _uiState.asStateFlow()

    private var nextUrl: String? = null

    init {
        loadMore()
    }

    fun loadMore() {
        val state = _uiState.value
        if (state.isLoading || state.endReached) return

        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            try {
                val page = getPokemonPage(nextUrl)
                nextUrl = page.nextUrl
                _uiState.update {
                    it.copy(
                        pokemon = it.pokemon + page.pokemon,
                        isLoading = false,
                        endReached = page.nextUrl == null
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message ?: "Something went wrong") }
            }
        }
    }
}
