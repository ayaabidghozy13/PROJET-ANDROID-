package com.example.projectandroid.ui.screens.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.projectandroid.data.model.Character
import com.example.projectandroid.data.model.Episode
import com.example.projectandroid.viewmodel.MainViewModel
import kotlinx.coroutines.delay

private val BackgroundColor = Color(0xFF080D18)
private val CardColor = Color(0xCC101928)
private val AccentColor = Color(0xFFB7FF4A)
private val MintColor = Color(0xFF78F5B2)
private val WhiteColor = Color(0xFFF7F9FF)

@Composable
fun CategoriesScreen(
    initialSection: String,
    onNavigateToDetails: (String, Int) -> Unit,
    onBackToHome: () -> Unit
) {
    val viewModel: MainViewModel = viewModel()

    val characters by viewModel.characters.collectAsState()
    val episodes by viewModel.episodes.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isLoadingMore by viewModel.isLoadingMore.collectAsState()
    val hasMoreCharacters by viewModel.hasMoreCharacters.collectAsState()
    val hasMoreEpisodes by viewModel.hasMoreEpisodes.collectAsState()
    val error by viewModel.error.collectAsState()

    var selectedSection by rememberSaveable(initialSection) {
        mutableStateOf(
            if (initialSection == "episodes") "episodes"
            else "characters"
        )
    }

    var characterSearch by rememberSaveable {
        mutableStateOf("")
    }

    var episodeSearch by rememberSaveable {
        mutableStateOf("")
    }

    var selectedStatus by rememberSaveable {
        mutableStateOf("all")
    }

    val activeSearch =
        if (selectedSection == "characters") {
            characterSearch
        } else {
            episodeSearch
        }

    val charactersListState = rememberLazyListState()
    val episodesListState = rememberLazyListState()

    val activeListState =
        if (selectedSection == "characters") {
            charactersListState
        } else {
            episodesListState
        }

    // Search and filters: reload page 1 after changes.
    LaunchedEffect(
        selectedSection,
        activeSearch,
        selectedStatus.takeIf { selectedSection == "characters" }
    ) {
        delay(400)
        activeListState.scrollToItem(0)

        if (selectedSection == "characters") {
            viewModel.loadCharacters(
                page = 1,
                name = characterSearch.trim().ifBlank { null },
                status = selectedStatus.takeIf { it != "all" }
            )
        } else {
            viewModel.loadEpisodes(
                page = 1,
                name = episodeSearch.trim().ifBlank { null }
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
    ) {
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize()
        ) {
            // Adapt the number of columns to the available screen width.
            val columns = when {
                maxWidth >= 900.dp -> 3
                maxWidth >= 600.dp -> 2
                else -> 1
            }

            LazyColumn(
                state = activeListState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = WindowInsets.statusBars
                        .asPaddingValues()
                        .calculateTopPadding() + 82.dp,
                    bottom = WindowInsets.navigationBars
                        .asPaddingValues()
                        .calculateBottomPadding() + 28.dp
                ),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // HEADER
                item {
                    Column(Modifier.fillMaxWidth()) {
                        Text(
                            text = "EXPLORE THE",
                            color = MintColor,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        )

                        Spacer(Modifier.height(5.dp))

                        Text(
                            text = "MULTIVERSE",
                            color = WhiteColor,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Black
                        )

                        Spacer(Modifier.height(6.dp))

                        Text(
                            text = "Browse characters and episodes from the Rick and Morty universe.",
                            color = WhiteColor.copy(alpha = 0.68f),
                            fontSize = 12.sp,
                            lineHeight = 18.sp
                        )

                        Spacer(Modifier.height(18.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            SectionButton(
                                text = "✦ CHARACTERS",
                                selected = selectedSection == "characters",
                                modifier = Modifier.weight(1f)
                            ) {
                                selectedSection = "characters"
                            }

                            SectionButton(
                                text = "▷ EPISODES",
                                selected = selectedSection == "episodes",
                                modifier = Modifier.weight(1f)
                            ) {
                                selectedSection = "episodes"
                            }
                        }
                    }
                }

                // SEARCH
                item {
                    Column(Modifier.fillMaxWidth()) {
                        Text(
                            text = if (selectedSection == "characters") {
                                "FIND A CHARACTER"
                            } else {
                                "FIND AN EPISODE"
                            },
                            color = MintColor,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.4.sp
                        )

                        Spacer(Modifier.height(8.dp))

                        OutlinedTextField(
                            value = activeSearch,
                            onValueChange = { newValue ->
                                if (selectedSection == "characters") {
                                    characterSearch = newValue
                                } else {
                                    episodeSearch = newValue
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(16.dp),
                            placeholder = {
                                Text(
                                    text = if (selectedSection == "characters") {
                                        "Search Rick, Morty, Summer..."
                                    } else {
                                        "Search Pilot, Rickmancing..."
                                    },
                                    color = WhiteColor.copy(alpha = 0.42f),
                                    fontSize = 12.sp
                                )
                            },
                            leadingIcon = {
                                Text(
                                    text = "⌕",
                                    color = AccentColor,
                                    fontSize = 25.sp
                                )
                            },
                            trailingIcon = {
                                if (activeSearch.isNotEmpty()) {
                                    Text(
                                        text = "×",
                                        modifier = Modifier
                                            .clickable {
                                                if (selectedSection == "characters") {
                                                    characterSearch = ""
                                                } else {
                                                    episodeSearch = ""
                                                }
                                            }
                                            .padding(
                                                horizontal = 12.dp,
                                                vertical = 6.dp
                                            ),
                                        color = WhiteColor,
                                        fontSize = 22.sp
                                    )
                                }
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = WhiteColor,
                                unfocusedTextColor = WhiteColor,
                                focusedBorderColor = AccentColor,
                                unfocusedBorderColor = WhiteColor.copy(alpha = 0.15f),
                                focusedContainerColor = CardColor,
                                unfocusedContainerColor = CardColor,
                                cursorColor = AccentColor
                            )
                        )

                        if (selectedSection == "characters") {
                            Spacer(Modifier.height(14.dp))

                            Text(
                                text = "LIFE STATUS",
                                color = WhiteColor.copy(alpha = 0.65f),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.2.sp
                            )

                            Spacer(Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                listOf(
                                    "ALL" to "all",
                                    "ALIVE" to "alive",
                                    "DEAD" to "dead",
                                    "UNKNOWN" to "unknown"
                                ).forEach { (label, value) ->
                                    StatusFilterButton(
                                        text = label,
                                        selected = selectedStatus == value,
                                        modifier = Modifier.weight(
                                            if (value == "unknown") 1.2f else 1f
                                        )
                                    ) {
                                        selectedStatus = value
                                    }
                                }
                            }
                        }
                    }
                }

                // DATABASE STATUS
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = CardColor
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = if (selectedSection == "characters") {
                                        "CHARACTER DATABASE"
                                    } else {
                                        "EPISODE DATABASE"
                                    },
                                    color = WhiteColor,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.2.sp
                                )

                                Spacer(Modifier.height(4.dp))

                                Text(
                                    text = if (selectedSection == "characters") {
                                        "${characters.size} results loaded"
                                    } else {
                                        "${episodes.size} results loaded"
                                    },
                                    color = WhiteColor.copy(alpha = 0.62f),
                                    fontSize = 11.sp
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(RoundedCornerShape(50))
                                    .background(
                                        when {
                                            isLoading || isLoadingMore ->
                                                Color(0xFFFFC857)
                                            error != null ->
                                                Color(0xFFFF5A68)
                                            else -> AccentColor
                                        }
                                    )
                            )
                        }
                    }
                }

                // FIRST-PAGE LOADING
                if (isLoading) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 30.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(color = AccentColor)

                            Spacer(Modifier.height(12.dp))

                            Text(
                                text = "Scanning the multiverse...",
                                color = WhiteColor.copy(alpha = 0.7f),
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                // ERROR
                if (!isLoading && error != null) {
                    item {
                        ErrorPanel(message = error ?: "Unknown error.")
                    }
                }

                // CHARACTER RESULTS
                if (
                    !isLoading &&
                    error == null &&
                    selectedSection == "characters"
                ) {
                    if (characters.isEmpty()) {
                        item {
                            EmptyResultsPanel(
                                message = "No characters found.",
                                suggestion = "Try another name or choose a different status."
                            )
                        }
                    } else {
                        items(
                            items = characters.chunked(columns),
                            key = { rowCharacters -> rowCharacters.first().id }
                        ) { rowCharacters ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                rowCharacters.forEach { character ->
                                    Box(modifier = Modifier.weight(1f)) {
                                        CharacterListCard(character = character) {
                                            onNavigateToDetails("character", character.id)
                                        }
                                    }
                                }

                                // Keep the last row aligned when it has fewer cards.
                                repeat(columns - rowCharacters.size) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }

                        // CHARACTER PAGINATION
                        if (hasMoreCharacters) {
                            item(key = "load_more_characters") {
                                LoadMoreButton(
                                    isLoading = isLoadingMore,
                                    onClick = {
                                        viewModel.loadMoreCharacters()
                                    }
                                )
                            }
                        }
                    }
                }

                // EPISODE RESULTS
                if (
                    !isLoading &&
                    error == null &&
                    selectedSection == "episodes"
                ) {
                    if (episodes.isEmpty()) {
                        item {
                            EmptyResultsPanel(
                                message = "No episodes found.",
                                suggestion = "Try a different episode title."
                            )
                        }
                    } else {
                        items(
                            items = episodes.chunked(columns),
                            key = { rowEpisodes -> rowEpisodes.first().id }
                        ) { rowEpisodes ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                rowEpisodes.forEach { episode ->
                                    Box(modifier = Modifier.weight(1f)) {
                                        EpisodeListCard(episode = episode) {
                                            onNavigateToDetails("episode", episode.id)
                                        }
                                    }
                                }

                                // Keep the last row aligned when it has fewer cards.
                                repeat(columns - rowEpisodes.size) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }

                        // EPISODE PAGINATION
                        if (hasMoreEpisodes) {
                            item(key = "load_more_episodes") {
                                LoadMoreButton(
                                    isLoading = isLoadingMore,
                                    onClick = {
                                        viewModel.loadMoreEpisodes()
                                    }
                                )
                            }
                        }
                    }
                }

                // FOOTER
                item {
                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = "MULTIVERSE SCAN COMPLETE • WUBBA LUBBA DUB DUB!",
                        modifier = Modifier.fillMaxWidth(),
                        color = WhiteColor.copy(alpha = 0.48f),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        ScrollIndicator(
            state = activeListState,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 5.dp)
                .zIndex(2f)
        )

        // Existing HOME button retained
        Button(
            onClick = onBackToHome,
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
                text = "← HOME",
                color = WhiteColor,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


// LOAD MORE BUTTON


@Composable
private fun LoadMoreButton(
    isLoading: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = !isLoading,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = AccentColor,
            contentColor = BackgroundColor,
            disabledContainerColor = AccentColor.copy(alpha = 0.45f),
            disabledContentColor = BackgroundColor
        ),
        contentPadding = PaddingValues(
            horizontal = 18.dp,
            vertical = 14.dp
        )
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(19.dp),
                color = BackgroundColor,
                strokeWidth = 2.dp
            )

            Spacer(Modifier.size(10.dp))

            Text(
                text = "LOADING MORE...",
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp
            )
        } else {
            Text(
                text = "＋  LOAD MORE",
                fontSize = 12.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.2.sp
            )
        }
    }
}


// SECTION BUTTON


@Composable
private fun SectionButton(
    text: String,
    selected: Boolean,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(
                if (selected) AccentColor.copy(alpha = 0.18f)
                else CardColor
            )
            .border(
                width = 1.dp,
                color = if (selected) AccentColor.copy(alpha = 0.55f)
                else Color.White.copy(alpha = 0.10f),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (selected) AccentColor
            else WhiteColor.copy(alpha = 0.72f),
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
    }
}

// STATUS FILTER BUTTON


@Composable
private fun StatusFilterButton(
    text: String,
    selected: Boolean,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (selected) AccentColor.copy(alpha = 0.17f)
                else CardColor
            )
            .border(
                width = 1.dp,
                color = if (selected) AccentColor.copy(alpha = 0.65f)
                else Color.White.copy(alpha = 0.10f),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 2.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (selected) AccentColor
            else WhiteColor.copy(alpha = 0.72f),
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1
        )
    }
}

// CHARACTER CARD


@Composable
private fun CharacterListCard(
    character: Character,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = CardColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = character.image,
                contentDescription = character.name,
                modifier = Modifier
                    .size(92.dp)
                    .clip(RoundedCornerShape(18.dp))
            )

            Spacer(Modifier.size(14.dp))

            Column(Modifier.weight(1f)) {
                Text(
                    text = character.name,
                    color = WhiteColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.height(5.dp))

                Text(
                    text = "${character.species} • ${character.gender}",
                    color = WhiteColor.copy(alpha = 0.65f),
                    fontSize = 11.sp
                )

                Spacer(Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(RoundedCornerShape(50))
                            .background(
                                when (character.status.lowercase()) {
                                    "alive" -> AccentColor
                                    "dead" -> Color(0xFFFF5A68)
                                    else -> Color(0xFFB8C1CC)
                                }
                            )
                    )

                    Spacer(Modifier.size(7.dp))

                    Text(
                        text = character.status.uppercase(),
                        color = WhiteColor.copy(alpha = 0.72f),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.7.sp
                    )
                }
            }

            Text(
                text = "›",
                color = AccentColor,
                fontSize = 28.sp,
                fontWeight = FontWeight.Light
            )
        }
    }
}

// EPISODE CARD


@Composable
private fun EpisodeListCard(
    episode: Episode,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = CardColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(AccentColor.copy(alpha = 0.13f))
                        .padding(horizontal = 10.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = episode.episode,
                        color = AccentColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black
                    )
                }

                Spacer(Modifier.size(10.dp))

                Text(
                    text = episode.name,
                    modifier = Modifier.weight(1f),
                    color = WhiteColor,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Black,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "›",
                    color = AccentColor,
                    fontSize = 28.sp
                )
            }

            Spacer(Modifier.height(12.dp))

            Text(
                text = episode.air_date,
                color = WhiteColor.copy(alpha = 0.62f),
                fontSize = 11.sp
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = "${episode.characters.size} characters in this episode",
                color = MintColor,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// EMPTY RESULTS


@Composable
private fun EmptyResultsPanel(
    message: String,
    suggestion: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "🛸", fontSize = 32.sp)

            Spacer(Modifier.height(10.dp))

            Text(
                text = message,
                color = WhiteColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(7.dp))

            Text(
                text = suggestion,
                color = WhiteColor.copy(alpha = 0.65f),
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}


// ERROR PANEL


@Composable
private fun ErrorPanel(message: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "MULTIVERSE CONNECTION ERROR",
                color = Color(0xFFFF6B78),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = message,
                color = WhiteColor.copy(alpha = 0.7f),
                fontSize = 11.sp
            )
        }
    }
}


// SCROLL INDICATOR


@Composable
private fun ScrollIndicator(
    state: LazyListState,
    modifier: Modifier = Modifier
) {
    val totalItems = state.layoutInfo.totalItemsCount
    val visibleItems = state.layoutInfo.visibleItemsInfo.size

    if (totalItems <= visibleItems || totalItems == 0) return

    val firstVisible = state.firstVisibleItemIndex
    val maxFirst = (totalItems - visibleItems).coerceAtLeast(1)
    val progress =
        (firstVisible.toFloat() / maxFirst.toFloat()).coerceIn(0f, 1f)

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(width = 18.dp, height = 150.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(CardColor)
                .border(
                    width = 1.dp,
                    color = Color.White.copy(alpha = 0.10f),
                    shape = RoundedCornerShape(12.dp)
                )
        ) {
            val thumbHeight = 42.dp
            val thumbTravel = 150.dp - thumbHeight - 8.dp

            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 4.dp + thumbTravel * progress)
                    .size(width = 8.dp, height = thumbHeight)
                    .clip(RoundedCornerShape(8.dp))
                    .background(AccentColor)
            )
        }

        Spacer(Modifier.height(5.dp))

        Text(
            text = "↕",
            color = AccentColor.copy(alpha = 0.8f),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}