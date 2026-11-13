
package com.example.projectandroid.data.repository

import com.example.projectandroid.data.model.ApiResponse
import com.example.projectandroid.data.model.Character
import com.example.projectandroid.data.model.Episode
import com.example.projectandroid.data.remote.RickAndMortyApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RickAndMortyRepository(
    private val api: RickAndMortyApi
) {

    fun getCharacters(
        page: Int = 1,
        name: String? = null,
        status: String? = null
    ): Flow<ApiResponse<Character>> = flow {

        val response = api.getCharacters(
            page = page,
            name = name,
            status = status
        )

        emit(response)
    }

    fun getEpisodes(
        page: Int = 1,
        name: String? = null
    ): Flow<ApiResponse<Episode>> = flow {

        val response = api.getEpisodes(
            page = page,
            name = name
        )

        emit(response)
    }

    fun getCharacter(id: Int): Flow<Character> = flow {
        emit(api.getCharacter(id))
    }

    fun getEpisode(id: Int): Flow<Episode> = flow {
        emit(api.getEpisode(id))
    }
}