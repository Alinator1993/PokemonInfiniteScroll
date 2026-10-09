package com.example.pokemonlist.domain.usecase

import com.example.pokemonlist.domain.model.PokemonPage
import com.example.pokemonlist.domain.repository.PokemonRepository
import javax.inject.Inject

class GetPokemonPageUseCase @Inject constructor(
    private val repository: PokemonRepository
) {
    suspend operator fun invoke(url: String? = null): PokemonPage {
        return repository.getPokemonPage(url)
    }
}
