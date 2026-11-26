package com.eliseu.pokedex.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeService {

    // Lista com o nome e a URL de todos os pokémons
    @GET("pokemon")
    suspend fun list(@Query("limit") limit: Int = 1025): PokemonListResponse

    // Detalhes de um pokémon
    @GET("pokemon/{id}")
    suspend fun detail(@Path("id") id: Int): PokemonDetail
}

object Api {
    val service: PokeService by lazy {
        Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PokeService::class.java)
    }
}
