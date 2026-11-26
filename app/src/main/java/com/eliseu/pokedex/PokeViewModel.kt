package com.eliseu.pokedex

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eliseu.pokedex.data.Api
import com.eliseu.pokedex.data.PokemonDetail
import com.eliseu.pokedex.data.PokemonItem
import kotlinx.coroutines.launch

sealed interface ListState {
    object Loading : ListState
    object Ready : ListState
    data class Error(val message: String) : ListState
}

sealed interface DetailState {
    object Loading : DetailState
    data class Success(val pokemon: PokemonDetail) : DetailState
    data class Error(val message: String) : DetailState
}

class PokeViewModel : ViewModel() {

    var listState by mutableStateOf<ListState>(ListState.Loading)
        private set

    var query by mutableStateOf("")
        private set

    // Pokémon aberto na tela de detalhes (null = está na lista)
    var selected by mutableStateOf<PokemonItem?>(null)
        private set

    var detailState by mutableStateOf<DetailState>(DetailState.Loading)
        private set

    private var all: List<PokemonItem> = emptyList()

    init {
        loadList()
    }

    fun loadList() {
        viewModelScope.launch {
            listState = ListState.Loading
            listState = try {
                all = Api.service.list().results
                ListState.Ready
            } catch (e: Exception) {
                ListState.Error("Não foi possível carregar os dados. Verifique sua internet e tente de novo.")
            }
        }
    }

    // Busca local: filtra por nome ou número enquanto o usuário digita
    fun filtered(): List<PokemonItem> {
        val q = query.trim().lowercase()
        val result = if (q.isEmpty()) {
            all
        } else {
            all.filter { it.name.contains(q) || it.id.toString() == q }
        }
        return result.take(60)
    }

    fun onQueryChange(text: String) {
        query = text
    }

    fun select(item: PokemonItem?) {
        selected = item
        if (item != null) loadDetail(item)
    }

    fun loadDetail(item: PokemonItem) {
        viewModelScope.launch {
            detailState = DetailState.Loading
            detailState = try {
                DetailState.Success(Api.service.detail(item.id))
            } catch (e: Exception) {
                DetailState.Error("Não foi possível carregar os detalhes.")
            }
        }
    }
}
