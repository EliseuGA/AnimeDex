package com.eliseu.animedex.data

import com.google.gson.annotations.SerializedName

// Respostas da API Jikan (https://docs.api.jikan.moe)

data class AnimeListResponse(
    val data: List<Anime> = emptyList()
)

data class Anime(
    @SerializedName("mal_id") val malId: Int,
    val title: String?,
    val synopsis: String?,
    val score: Double?,
    val episodes: Int?,
    val year: Int?,
    val status: String?,
    val images: Images?,
    val genres: List<Genre>?
) {
    val imageUrl: String?
        get() = images?.jpg?.largeImageUrl ?: images?.jpg?.imageUrl
}

data class Images(val jpg: Jpg?)

data class Jpg(
    @SerializedName("image_url") val imageUrl: String?,
    @SerializedName("large_image_url") val largeImageUrl: String?
)

data class Genre(val name: String)
