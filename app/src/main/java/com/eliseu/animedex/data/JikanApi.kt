package com.eliseu.animedex.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface JikanService {

    // Animes mais bem avaliados
    @GET("top/anime")
    suspend fun topAnime(@Query("limit") limit: Int = 20): AnimeListResponse

    // Busca por nome
    @GET("anime")
    suspend fun search(
        @Query("q") query: String,
        @Query("limit") limit: Int = 20,
        @Query("sfw") sfw: Boolean = true
    ): AnimeListResponse
}

object Api {
    val service: JikanService by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.jikan.moe/v4/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(JikanService::class.java)
    }
}
