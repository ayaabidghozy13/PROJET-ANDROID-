package com.example.projectandroid.ui.screens.details

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.projectandroid.data.model.Character
import com.example.projectandroid.data.model.Episode
import com.example.projectandroid.data.remote.RickAndMortyApiClient
import com.example.projectandroid.viewmodel.FavoritesViewModel
import com.example.projectandroid.viewmodel.MainViewModel
import kotlinx.coroutines.CancellationException

private val SpaceBlack = Color(0xFF080D18)
private val PortalGreen = Color(0xFFB7FF4A)
private val PortalMint = Color(0xFF78F5B2)
private val SpaceWhite = Color(0xFFF7F9FF)
private val GlassColor = Color(0xCC101928)

@Composable
fun DetailsScreen(
    type: String,
    id: Int,
    onBack: () -> Unit,
    onCharacterClick: (Int) -> Unit = {}
) {
    val viewModel: MainViewModel = viewModel()
    val favoritesViewModel: FavoritesViewModel = viewModel()

    BackHandler {
        onBack()
    }

    /*
     * Load the requested detail.
     */
    LaunchedEffect(type, id) {
        if (type == "episode") {
            viewModel.loadEpisode(id)
        } else {
            viewModel.loadCharacter(id)
        }
    }

    /*
     * Main detail state.
     */
    val character by viewModel.selectedCharacter.collectAsState()
    val episode by viewModel.selectedEpisode.collectAsState()
    val isLoading by viewModel.isDetailLoading.collectAsState()
    val error by viewModel.detailError.collectAsState()

    /*
     * Persistent favorites.
     *
     * This data comes from DataStore, not the API.
     */
    val favorites by favoritesViewModel.favorites
        .collectAsStateWithLifecycle()

    /*
     * Determine whether the currently displayed character
     * is already a favorite.
     */
    val isFavorite = character?.let { currentCharacter ->
        favorites.any {
            it.id == currentCharacter.id
        }
    } ?: false

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SpaceBlack)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = WindowInsets.statusBars
                        .asPaddingValues()
                        .calculateTopPadding() + 82.dp,
                    bottom = WindowInsets.navigationBars
                        .asPaddingValues()
                        .calculateBottomPadding() + 30.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            /*
             * LOADING
             */
            if (isLoading) {

                Spacer(
                    modifier = Modifier.size(90.dp)
                )

                CircularProgressIndicator(
                    color = PortalGreen
                )

                Spacer(
                    modifier = Modifier.size(14.dp)
                )

                Text(
                    text = "Opening portal...",
                    color = SpaceWhite.copy(alpha = 0.7f),
                    fontSize = 12.sp
                )
            }

            /*
             * ERROR
             */
            else if (error != null) {

                Spacer(
                    modifier = Modifier.size(50.dp)
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = GlassColor
                    )
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(22.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = "PORTAL ERROR",
                            color = Color(0xFFFF6B78),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black
                        )

                        Spacer(
                            modifier = Modifier.size(10.dp)
                        )

                        Text(
                            text = error
                                ?: "Unable to load details.",
                            color = SpaceWhite.copy(alpha = 0.72f),
                            fontSize = 11.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            /*
             * CHARACTER
             */
            else if (
                type == "character" &&
                character != null
            ) {

                CharacterDetails(
                    character = character!!,
                    isFavorite = isFavorite,
                    onFavoriteClick = {
                        favoritesViewModel.toggleFavorite(
                            character!!
                        )
                    }
                )
            }

            /*
             * EPISODE
             */
            else if (
                type == "episode" &&
                episode != null
            ) {

                EpisodeDetails(
                    episode = episode!!,
                    onCharacterClick = onCharacterClick
                )
            }
        }

        /*
         * BACK BUTTON
         */
        Button(
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(
                    start = 16.dp,
                    top = WindowInsets.statusBars
                        .asPaddingValues()
                        .calculateTopPadding() + 12.dp
                )
                .zIndex(10f),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xD9101928)
            )
        ) {

            Text(
                text = "← BACK",
                color = SpaceWhite,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// ================================================================
// CHARACTER DETAILS
// ================================================================

@Composable
private fun CharacterDetails(
    character: Character,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit
) {

    Text(
        text = "CHARACTER PROFILE",
        color = PortalMint,
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 2.sp
    )

    Spacer(
        modifier = Modifier.size(8.dp)
    )

    /*
     * CHARACTER NAME + FAVORITE BUTTON
     */
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = character.name,
            modifier = Modifier.weight(1f),
            color = SpaceWhite,
            fontSize = 30.sp,
            fontWeight = FontWeight.Black
        )

        Spacer(
            modifier = Modifier.width(12.dp)
        )

        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(
                    if (isFavorite) {
                        PortalGreen.copy(alpha = 0.18f)
                    } else {
                        Color.White.copy(alpha = 0.06f)
                    }
                )
                .border(
                    width = 1.5.dp,
                    color = if (isFavorite) {
                        PortalGreen.copy(alpha = 0.7f)
                    } else {
                        Color.White.copy(alpha = 0.18f)
                    },
                    shape = CircleShape
                )
                .clickable {
                    onFavoriteClick()
                },
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = if (isFavorite) {
                    "♥"
                } else {
                    "♡"
                },
                color = if (isFavorite) {
                    PortalGreen
                } else {
                    SpaceWhite.copy(alpha = 0.75f)
                },
                fontSize = 27.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }

    Spacer(
        modifier = Modifier.size(18.dp)
    )

    /*
     * CHARACTER IMAGE
     */
    AsyncImage(
        model = character.image,
        contentDescription = character.name,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(240.dp)
            .clip(RoundedCornerShape(28.dp))
            .border(
                width = 2.dp,
                color = PortalGreen.copy(alpha = 0.45f),
                shape = RoundedCornerShape(28.dp)
            )
    )

    Spacer(
        modifier = Modifier.size(20.dp)
    )

    /*
     * STATUS
     */
    StatusBadge(
        status = character.status
    )

    Spacer(
        modifier = Modifier.size(16.dp)
    )

    /*
     * SPECIES
     */
    InfoCard(
        title = "SPECIES",
        value = character.species
    )

    Spacer(
        modifier = Modifier.size(10.dp)
    )

    /*
     * GENDER
     */
    InfoCard(
        title = "GENDER",
        value = character.gender
    )

    Spacer(
        modifier = Modifier.size(10.dp)
    )

    /*
     * TYPE
     */
    InfoCard(
        title = "TYPE",
        value = if (character.type.isBlank()) {
            "No specific type"
        } else {
            character.type
        }
    )

    Spacer(
        modifier = Modifier.size(10.dp)
    )

    /*
     * ORIGIN
     */
    InfoCard(
        title = "ORIGIN",
        value = character.origin.name
    )

    Spacer(
        modifier = Modifier.size(10.dp)
    )

    /*
     * CURRENT LOCATION
     */
    InfoCard(
        title = "CURRENT LOCATION",
        value = character.location.name
    )

    Spacer(
        modifier = Modifier.size(10.dp)
    )

    /*
     * EPISODES
     */
    InfoCard(
        title = "EPISODES",
        value = "${character.episode.size} appearances"
    )

    Spacer(
        modifier = Modifier.size(18.dp)
    )

    /*
     * FAVORITE STATUS MESSAGE
     */
    Text(
        text = if (isFavorite) {
            "♥ SAVED TO YOUR COLLECTION"
        } else {
            "♡ TAP THE HEART TO SAVE THIS CHARACTER"
        },
        color = if (isFavorite) {
            PortalGreen
        } else {
            SpaceWhite.copy(alpha = 0.5f)
        },
        fontSize = 9.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.8.sp,
        textAlign = TextAlign.Center
    )
}

// ================================================================
// EPISODE DETAILS + OPTIMIZED CHARACTER PORTRAITS
// ================================================================

@Composable
private fun EpisodeDetails(
    episode: Episode,
    onCharacterClick: (Int) -> Unit
) {

    var episodeCharacters by remember(episode.id) {
        mutableStateOf<List<Character>>(emptyList())
    }

    var isLoadingCharacters by remember(episode.id) {
        mutableStateOf(true)
    }

    var charactersError by remember(episode.id) {
        mutableStateOf(false)
    }

    /*
     * Fetch all episode characters in ONE bulk request.
     *
     * This avoids making one HTTP request for every
     * character in the episode.
     */
    LaunchedEffect(episode.id) {

        isLoadingCharacters = true
        charactersError = false
        episodeCharacters = emptyList()

        try {

            val characterIds = episode.characters
                .mapNotNull { url ->

                    Regex("""/character/(\d+)/?$""")
                        .find(url)
                        ?.groupValues
                        ?.get(1)
                        ?.toIntOrNull()
                }
                .distinct()

            val api = RickAndMortyApiClient.api

            episodeCharacters = when {

                characterIds.isEmpty() -> {
                    emptyList()
                }

                characterIds.size == 1 -> {
                    listOf(
                        api.getCharacter(
                            characterIds.first()
                        )
                    )
                }

                else -> {
                    api.getCharactersByIds(
                        characterIds.joinToString(",")
                    )
                }
            }

        } catch (exception: CancellationException) {

            throw exception

        } catch (exception: Exception) {

            charactersError = true

        } finally {

            isLoadingCharacters = false
        }
    }

    Text(
        text = "EPISODE PROFILE",
        color = PortalMint,
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 2.sp
    )

    Spacer(
        modifier = Modifier.size(8.dp)
    )

    Text(
        text = episode.name,
        color = SpaceWhite,
        fontSize = 28.sp,
        fontWeight = FontWeight.Black,
        textAlign = TextAlign.Center
    )

    Spacer(
        modifier = Modifier.size(20.dp)
    )

    /*
     * EPISODE CODE
     */
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(18.dp))
            .background(
                PortalGreen.copy(alpha = 0.14f)
            )
            .border(
                width = 1.dp,
                color = PortalGreen.copy(alpha = 0.40f),
                shape = RoundedCornerShape(18.dp)
            )
            .padding(
                horizontal = 20.dp,
                vertical = 14.dp
            )
    ) {

        Text(
            text = episode.episode,
            color = PortalGreen,
            fontSize = 17.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.sp
        )
    }

    Spacer(
        modifier = Modifier.size(20.dp)
    )

    /*
     * AIR DATE
     */
    InfoCard(
        title = "AIR DATE",
        value = episode.air_date
    )

    Spacer(
        modifier = Modifier.size(10.dp)
    )

    /*
     * CHARACTER COUNT
     */
    InfoCard(
        title = "CHARACTERS",
        value = "${episode.characters.size} characters appear"
    )

    Spacer(
        modifier = Modifier.size(22.dp)
    )

    /*
     * CHARACTER PORTRAITS CARD
     */
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = GlassColor
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 18.dp)
        ) {

            Text(
                text = "CHARACTERS IN THIS EPISODE",
                modifier = Modifier.padding(
                    horizontal = 18.dp
                ),
                color = PortalGreen,
                fontSize = 12.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp
            )

            Spacer(
                modifier = Modifier.size(6.dp)
            )

            Text(
                text = "Tap a character to explore their profile",
                modifier = Modifier.padding(
                    horizontal = 18.dp
                ),
                color = SpaceWhite.copy(alpha = 0.60f),
                fontSize = 11.sp
            )

            Spacer(
                modifier = Modifier.size(16.dp)
            )

            when {

                /*
                 * LOADING PORTRAITS
                 */
                isLoadingCharacters -> {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {

                        CircularProgressIndicator(
                            color = PortalGreen,
                            modifier = Modifier.size(28.dp),
                            strokeWidth = 3.dp
                        )
                    }
                }

                /*
                 * PORTRAIT ERROR
                 */
                charactersError -> {

                    Text(
                        text = "Unable to load character portraits. Please try again later.",
                        modifier = Modifier.padding(
                            horizontal = 18.dp
                        ),
                        color = Color(0xFFFF8A8A),
                        fontSize = 12.sp
                    )
                }

                /*
                 * NO CHARACTERS
                 */
                episodeCharacters.isEmpty() -> {

                    Text(
                        text = "No characters found for this episode.",
                        modifier = Modifier.padding(
                            horizontal = 18.dp
                        ),
                        color = SpaceWhite.copy(alpha = 0.65f),
                        fontSize = 12.sp
                    )
                }

                /*
                 * CHARACTER PORTRAITS
                 */
                else -> {

                    LazyRow(
                        horizontalArrangement =
                            Arrangement.spacedBy(14.dp),
                        contentPadding =
                            PaddingValues(
                                horizontal = 18.dp
                            )
                    ) {

                        items(
                            items = episodeCharacters,
                            key = { character ->
                                character.id
                            }
                        ) { character ->

                            Column(
                                modifier = Modifier
                                    .width(82.dp)
                                    .clip(
                                        RoundedCornerShape(14.dp)
                                    )
                                    .clickable {
                                        onCharacterClick(
                                            character.id
                                        )
                                    }
                                    .padding(5.dp),
                                horizontalAlignment =
                                    Alignment.CenterHorizontally
                            ) {

                                AsyncImage(
                                    model = character.image,
                                    contentDescription =
                                        character.name,
                                    contentScale =
                                        ContentScale.Crop,
                                    modifier = Modifier
                                        .size(70.dp)
                                        .clip(CircleShape)
                                        .border(
                                            width = 2.dp,
                                            color = PortalGreen
                                                .copy(alpha = 0.75f),
                                            shape = CircleShape
                                        )
                                )

                                Spacer(
                                    modifier = Modifier.size(8.dp)
                                )

                                Text(
                                    text = character.name,
                                    color = SpaceWhite,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium,
                                    textAlign = TextAlign.Center,
                                    maxLines = 2,
                                    lineHeight = 14.sp
                                )
                            }
                        }
                    }

                    Spacer(
                        modifier = Modifier.size(8.dp)
                    )

                    Text(
                        text = "← Swipe to discover more →",
                        modifier = Modifier.fillMaxWidth(),
                        color = PortalMint.copy(alpha = 0.75f),
                        fontSize = 10.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }

    Spacer(
        modifier = Modifier.size(20.dp)
    )

    /*
     * EPISODE INFORMATION
     */
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = GlassColor
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {

            Text(
                text = "EPISODE INFORMATION",
                color = PortalMint,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.4.sp
            )

            Spacer(
                modifier = Modifier.size(10.dp)
            )

            Text(
                text = "This episode contains data retrieved from the Rick and Morty API.",
                color = SpaceWhite.copy(alpha = 0.68f),
                fontSize = 12.sp,
                lineHeight = 18.sp
            )
        }
    }
}

// ================================================================
// STATUS BADGE
// ================================================================

@Composable
private fun StatusBadge(
    status: String
) {

    val statusColor = when (
        status.lowercase()
    ) {
        "alive" -> PortalGreen
        "dead" -> Color(0xFFFF5A68)
        else -> Color(0xFFB8C1CC)
    }

    Row(
        modifier = Modifier
            .clip(
                RoundedCornerShape(50.dp)
            )
            .background(
                statusColor.copy(alpha = 0.12f)
            )
            .border(
                width = 1.dp,
                color = statusColor.copy(alpha = 0.40f),
                shape = RoundedCornerShape(50.dp)
            )
            .padding(
                horizontal = 14.dp,
                vertical = 8.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(statusColor)
        )

        Spacer(
            modifier = Modifier.size(7.dp)
        )

        Text(
            text = status.uppercase(),
            color = statusColor,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
    }
}

// ================================================================
// INFO CARD
// ================================================================

@Composable
private fun InfoCard(
    title: String,
    value: String
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = GlassColor
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 17.dp,
                    vertical = 15.dp
                ),
            horizontalArrangement =
                Arrangement.SpaceBetween,
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Text(
                text = title,
                color = PortalMint,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            Spacer(
                modifier = Modifier.size(15.dp)
            )

            Text(
                text = value,
                modifier = Modifier.weight(1f),
                color = SpaceWhite,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.End
            )
        }
    }
}