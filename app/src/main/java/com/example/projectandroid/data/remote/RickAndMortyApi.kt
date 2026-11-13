
package com.example.projectandroid.data.remote

import com.example.projectandroid.data.model.ApiResponse
import com.example.projectandroid.data.model.Character
import com.example.projectandroid.data.model.Episode
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit interface for the Rick and Morty REST API.
 *
 * Base URL:
 * https://rickandmortyapi.com/api/
 */
interface RickAndMortyApi {

    // ---------------------------------------------------------
    // CHARACTERS
    // ---------------------------------------------------------

    // Fetch a page of characters with optional filters.
    @GET("character")
    suspend fun getCharacters(
        @Query("page") page: Int = 1,
        @Query("name") name: String? = null,
        @Query("status") status: String? = null,
        @Query("species") species: String? = null,
        @Query("type") type: String? = null,
        @Query("gender") gender: String? = null
    ): ApiResponse<Character>

    // Fetch one character by ID.
    @GET("character/{id}")
    suspend fun getCharacter(
        @Path("id") id: Int
    ): Character

    // Fetch multiple characters in ONE request.
    // Example endpoint: /character/1,2,3
    @GET("character/{ids}")
    suspend fun getCharactersByIds(
        @Path("ids") ids: String
    ): List<Character>

    // ---------------------------------------------------------
    // EPISODES
    // ---------------------------------------------------------

    // Fetch a page of episodes with optional filters.
    @GET("episode")
    suspend fun getEpisodes(
        @Query("page") page: Int = 1,
        @Query("name") name: String? = null,
        @Query("episode") episode: String? = null
    ): ApiResponse<Episode>

    // Fetch one episode by ID.
    @GET("episode/{id}")
    suspend fun getEpisode(
        @Path("id") id: Int
    ): Episode
}

/**
 * Single Retrofit instance used by the application.
 */
object RickAndMortyApiClient {

    private const val BASE_URL =
        "https://rickandmortyapi.com/api/"

    val api: RickAndMortyApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
            .create(RickAndMortyApi::class.java)
    }
}