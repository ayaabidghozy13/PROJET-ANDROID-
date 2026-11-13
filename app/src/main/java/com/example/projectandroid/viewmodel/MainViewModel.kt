
package com.example.projectandroid.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectandroid.data.model.Character
import com.example.projectandroid.data.model.Episode
import com.example.projectandroid.data.remote.RickAndMortyApiClient
import com.example.projectandroid.data.repository.RickAndMortyRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val repository = RickAndMortyRepository(
        RickAndMortyApiClient.api
    )

    // Characters
    private val _characters =
        MutableStateFlow<List<Character>>(emptyList())
    val characters: StateFlow<List<Character>> =
        _characters.asStateFlow()

    // Episodes
    private val _episodes =
        MutableStateFlow<List<Episode>>(emptyList())
    val episodes: StateFlow<List<Episode>> =
        _episodes.asStateFlow()

    // General loading and error states
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> =
        _isLoadingMore.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // Pagination state
    private val _hasMoreCharacters = MutableStateFlow(true)
    val hasMoreCharacters: StateFlow<Boolean> =
        _hasMoreCharacters.asStateFlow()

    private val _hasMoreEpisodes = MutableStateFlow(true)
    val hasMoreEpisodes: StateFlow<Boolean> =
        _hasMoreEpisodes.asStateFlow()

    private var characterPage = 1
    private var episodePage = 1

    private var characterName: String? = null
    private var characterStatus: String? = null
    private var episodeName: String? = null

    private var characterJob: Job? = null
    private var episodeJob: Job? = null
    private var characterMoreJob: Job? = null
    private var episodeMoreJob: Job? = null

    // Detail states
    private val _selectedCharacter =
        MutableStateFlow<Character?>(null)
    val selectedCharacter: StateFlow<Character?> =
        _selectedCharacter.asStateFlow()

    private val _selectedEpisode =
        MutableStateFlow<Episode?>(null)
    val selectedEpisode: StateFlow<Episode?> =
        _selectedEpisode.asStateFlow()

    private val _isDetailLoading = MutableStateFlow(false)
    val isDetailLoading: StateFlow<Boolean> =
        _isDetailLoading.asStateFlow()

    private val _detailError = MutableStateFlow<String?>(null)
    val detailError: StateFlow<String?> =
        _detailError.asStateFlow()

    // Load or refresh characters from page 1
    fun loadCharacters(
        page: Int = 1,
        name: String? = null,
        status: String? = null
    ) {
        if (page != 1) return

        characterJob?.cancel()
        characterMoreJob?.cancel()

        characterName = name?.trim()?.takeIf { it.isNotEmpty() }
        characterStatus = status?.trim()?.takeIf { it.isNotEmpty() }

        characterPage = 1
        _hasMoreCharacters.value = true
        _characters.value = emptyList()
        _error.value = null
        _isLoading.value = true
        _isLoadingMore.value = false

        characterJob = viewModelScope.launch {
            try {
                val response = repository.getCharacters(
                    page = 1,
                    name = characterName,
                    status = characterStatus
                )

                response.collect { result ->
                    _characters.value = result.results
                    characterPage = 1
                    _hasMoreCharacters.value =
                        result.info.next != null
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _error.value =
                    exception.message ?: "Unable to load characters."
            } finally {
                if (characterJob == kotlinx.coroutines.currentCoroutineContext()[Job]) {
                    _isLoading.value = false
                }
            }
        }
    }

    // Append the next page of characters
    fun loadMoreCharacters() {
        if (_isLoading.value ||
            _isLoadingMore.value ||
            !_hasMoreCharacters.value
        ) return

        characterMoreJob?.cancel()
        _isLoadingMore.value = true
        _error.value = null

        val nextPage = characterPage + 1
        val requestedName = characterName
        val requestedStatus = characterStatus

        characterMoreJob = viewModelScope.launch {
            try {
                repository.getCharacters(
                    page = nextPage,
                    name = requestedName,
                    status = requestedStatus
                ).collect { result ->
                    _characters.value =
                        (_characters.value + result.results)
                            .distinctBy { it.id }

                    characterPage = nextPage
                    _hasMoreCharacters.value =
                        result.info.next != null
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _error.value =
                    exception.message ?: "Unable to load more characters."
            } finally {
                _isLoadingMore.value = false
            }
        }
    }

    // Load or refresh episodes from page 1
    fun loadEpisodes(
        page: Int = 1,
        name: String? = null
    ) {
        if (page != 1) return

        episodeJob?.cancel()
        episodeMoreJob?.cancel()

        episodeName = name?.trim()?.takeIf { it.isNotEmpty() }

        episodePage = 1
        _hasMoreEpisodes.value = true
        _episodes.value = emptyList()
        _error.value = null
        _isLoading.value = true
        _isLoadingMore.value = false

        episodeJob = viewModelScope.launch {
            try {
                repository.getEpisodes(
                    page = 1,
                    name = episodeName
                ).collect { result ->
                    _episodes.value = result.results
                    episodePage = 1
                    _hasMoreEpisodes.value =
                        result.info.next != null
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _error.value =
                    exception.message ?: "Unable to load episodes."
            } finally {
                if (episodeJob == kotlinx.coroutines.currentCoroutineContext()[Job]) {
                    _isLoading.value = false
                }
            }
        }
    }

    // Append the next page of episodes
    fun loadMoreEpisodes() {
        if (_isLoading.value ||
            _isLoadingMore.value ||
            !_hasMoreEpisodes.value
        ) return

        episodeMoreJob?.cancel()
        _isLoadingMore.value = true
        _error.value = null

        val nextPage = episodePage + 1
        val requestedName = episodeName

        episodeMoreJob = viewModelScope.launch {
            try {
                repository.getEpisodes(
                    page = nextPage,
                    name = requestedName
                ).collect { result ->
                    _episodes.value =
                        (_episodes.value + result.results)
                            .distinctBy { it.id }

                    episodePage = nextPage
                    _hasMoreEpisodes.value =
                        result.info.next != null
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _error.value =
                    exception.message ?: "Unable to load more episodes."
            } finally {
                _isLoadingMore.value = false
            }
        }
    }

    // Load one character
    fun loadCharacter(id: Int) {
        viewModelScope.launch {
            _isDetailLoading.value = true
            _detailError.value = null
            _selectedCharacter.value = null
            _selectedEpisode.value = null

            try {
                repository.getCharacter(id).collect { character ->
                    _selectedCharacter.value = character
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _detailError.value =
                    exception.message ?: "Unable to load character."
            } finally {
                _isDetailLoading.value = false
            }
        }
    }

    // Load one episode
    fun loadEpisode(id: Int) {
        viewModelScope.launch {
            _isDetailLoading.value = true
            _detailError.value = null
            _selectedCharacter.value = null
            _selectedEpisode.value = null

            try {
                repository.getEpisode(id).collect { episode ->
                    _selectedEpisode.value = episode
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _detailError.value =
                    exception.message ?: "Unable to load episode."
            } finally {
                _isDetailLoading.value = false
            }
        }
    }
}