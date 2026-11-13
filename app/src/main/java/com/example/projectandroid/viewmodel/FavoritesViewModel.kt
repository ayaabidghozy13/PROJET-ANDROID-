package com.example.projectandroid.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectandroid.data.model.Character
import com.example.projectandroid.data.model.FavoriteCharacter
import com.example.projectandroid.data.repository.FavoritesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoritesViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository =
        FavoritesRepository(application)

    val favorites: StateFlow<List<FavoriteCharacter>> =
        repository.favorites.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun toggleFavorite(
        character: Character
    ) {
        viewModelScope.launch {
            repository.toggleFavorite(
                character.toFavoriteCharacter()
            )
        }
    }

    fun toggleFavorite(
        favorite: FavoriteCharacter
    ) {
        viewModelScope.launch {
            repository.toggleFavorite(favorite)
        }
    }
}

fun Character.toFavoriteCharacter(): FavoriteCharacter {
    return FavoriteCharacter(
        id = id,
        name = name,
        status = status,
        species = species,
        image = image
    )
}