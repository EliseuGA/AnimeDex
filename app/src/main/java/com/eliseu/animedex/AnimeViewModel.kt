package com.eliseu.animedex

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eliseu.animedex.data.Anime
import com.eliseu.animedex.data.Api
import kotlinx.coroutines.launch

sealed interface UiState {
    object Loading : UiState
    data class Success(val items: List<Anime>) : UiState
    data class Error(val message: String) : UiState
}

class AnimeViewModel : ViewModel() {

    var state by mutableStateOf<UiState>(UiState.Loading)
        private set

    var query by mutableStateOf("")
        private set

    // Anime aberto na tela de detalhes (null = está na lista)
    var selected by mutableStateOf<Anime?>(null)
        private set

    init {
        load()
    }

    fun onQueryChange(text: String) {
        query = text
    }

    fun select(anime: Anime?) {
        selected = anime
    }

    fun load() {
        viewModelScope.launch {
            state = UiState.Loading
            state = try {
                val q = query.trim()
                val response = if (q.isEmpty()) {
                    Api.service.topAnime()
                } else {
                    Api.service.search(q)
                }
                UiState.Success(response.data.distinctBy { it.malId })
            } catch (e: Exception) {
                UiState.Error("Não foi possível carregar os dados. Verifique sua internet e tente de novo.")
            }
        }
    }
}
