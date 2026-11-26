package com.eliseu.pokedex.data

import com.google.gson.annotations.SerializedName

// Respostas da PokéAPI (https://pokeapi.co)

data class PokemonListResponse(
    val results: List<PokemonItem> = emptyList()
)

data class PokemonItem(
    val name: String,
    val url: String
) {
    // O id vem no final da URL: https://pokeapi.co/api/v2/pokemon/25/
    val id: Int
        get() = url.trimEnd('/').substringAfterLast('/').toIntOrNull() ?: 0

    val imageUrl: String
        get() = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"

    val displayName: String
        get() = name.replaceFirstChar { it.uppercase() }
}

data class PokemonDetail(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val types: List<TypeSlot> = emptyList(),
    val abilities: List<AbilitySlot> = emptyList(),
    val stats: List<StatSlot> = emptyList()
)

data class NamedResource(val name: String)

data class TypeSlot(val type: NamedResource)

data class AbilitySlot(val ability: NamedResource)

data class StatSlot(
    @SerializedName("base_stat") val baseStat: Int,
    val stat: NamedResource
)
