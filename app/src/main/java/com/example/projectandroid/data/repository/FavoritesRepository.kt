package com.example.projectandroid.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.projectandroid.data.model.FavoriteCharacter
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.favoritesDataStore by preferencesDataStore(
    name = "favorites"
)

class FavoritesRepository(
    private val context: Context
) {

    private val gson = Gson()

    private val favoritesKey =
        stringSetPreferencesKey("favorite_characters")

    val favorites: Flow<List<FavoriteCharacter>> =
        context.favoritesDataStore.data.map { preferences ->

            preferences[favoritesKey]
                .orEmpty()
                .mapNotNull { json ->
                    try {
                        gson.fromJson(
                            json,
                            FavoriteCharacter::class.java
                        )
                    } catch (_: Exception) {
                        null
                    }
                }
                .distinctBy { it.id }
                .sortedBy { it.name.lowercase() }
        }

    suspend fun addFavorite(
        character: FavoriteCharacter
    ) {
        context.favoritesDataStore.edit { preferences ->

            val current =
                preferences[favoritesKey]
                    ?.toMutableSet()
                    ?: mutableSetOf()

            current.removeAll { json ->
                try {
                    gson.fromJson(
                        json,
                        FavoriteCharacter::class.java
                    ).id == character.id
                } catch (_: Exception) {
                    false
                }
            }

            current.add(
                gson.toJson(character)
            )

            preferences[favoritesKey] = current
        }
    }

    suspend fun removeFavorite(
        characterId: Int
    ) {
        context.favoritesDataStore.edit { preferences ->

            val current =
                preferences[favoritesKey]
                    ?.toMutableSet()
                    ?: mutableSetOf()

            current.removeAll { json ->
                try {
                    gson.fromJson(
                        json,
                        FavoriteCharacter::class.java
                    ).id == characterId
                } catch (_: Exception) {
                    false
                }
            }

            if (current.isEmpty()) {
                preferences.remove(favoritesKey)
            } else {
                preferences[favoritesKey] = current
            }
        }
    }

    suspend fun toggleFavorite(
        character: FavoriteCharacter
    ) {
        context.favoritesDataStore.edit { preferences ->

            val current =
                preferences[favoritesKey]
                    ?.toMutableSet()
                    ?: mutableSetOf()

            val existingJson = current.firstOrNull { json ->
                try {
                    gson.fromJson(
                        json,
                        FavoriteCharacter::class.java
                    ).id == character.id
                } catch (_: Exception) {
                    false
                }
            }

            if (existingJson != null) {
                current.remove(existingJson)
            } else {
                current.add(
                    gson.toJson(character)
                )
            }

            if (current.isEmpty()) {
                preferences.remove(favoritesKey)
            } else {
                preferences[favoritesKey] = current
            }
        }
    }
}