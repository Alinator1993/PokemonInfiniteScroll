package com.example.pokemonlist.presentation.pokemon

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pokemonlist.presentation.components.InfiniteScrollList

@Composable
fun PokemonScreen(
    modifier: Modifier = Modifier,
    viewModel: PokemonViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    InfiniteScrollList(
        items = uiState.pokemon,
        isLoading = uiState.isLoading,
        error = uiState.error,
        onLoadMore = viewModel::loadMore,
        modifier = modifier.fillMaxSize()
    ) { pokemon ->
        Text(
            text = pokemon.name.replaceFirstChar { it.uppercase() },
            modifier = Modifier.padding(16.dp)
        )
        HorizontalDivider()
    }
}
