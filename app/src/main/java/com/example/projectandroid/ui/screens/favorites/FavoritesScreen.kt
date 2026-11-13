package com.example.projectandroid.ui.screens.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.projectandroid.data.model.FavoriteCharacter
import com.example.projectandroid.viewmodel.FavoritesViewModel

private val SpaceBlack = Color(0xFF080D18)
private val PortalGreen = Color(0xFFB7FF4A)
private val PortalMint = Color(0xFF78F5B2)
private val SpaceWhite = Color(0xFFF7F9FF)
private val GlassColor = Color(0xE9101928)

@Composable
fun FavoritesScreen(
    onBack: () -> Unit,
    onCharacterClick: (Int) -> Unit,
    viewModel: FavoritesViewModel = viewModel()
) {

    val favorites by viewModel.favorites
        .collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SpaceBlack)
    ) {

        /*
         * HEADER
         */
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 18.dp,
                    end = 18.dp,
                    top = 22.dp,
                    bottom = 12.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(
                        PortalGreen.copy(alpha = 0.12f)
                    )
                    .border(
                        1.dp,
                        PortalGreen.copy(alpha = 0.45f),
                        CircleShape
                    )
                    .clickable { onBack() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "‹",
                    color = PortalGreen,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(
                modifier = Modifier.width(14.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = "YOUR COLLECTION",
                    color = PortalMint,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )

                Spacer(
                    modifier = Modifier.height(3.dp)
                )

                Text(
                    text = "FAVORITES",
                    color = SpaceWhite,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Black
                )
            }

            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(
                        PortalGreen.copy(alpha = 0.14f)
                    )
                    .border(
                        1.dp,
                        PortalGreen.copy(alpha = 0.4f),
                        CircleShape
                    )
                    .padding(
                        horizontal = 13.dp,
                        vertical = 8.dp
                    ),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = favorites.size.toString(),
                    color = PortalGreen,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }

        /*
         * EMPTY STATE
         */
        if (favorites.isEmpty()) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .shadow(
                            elevation = 15.dp,
                            shape = CircleShape
                        )
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    PortalGreen.copy(alpha = 0.22f),
                                    Color.Transparent
                                )
                            )
                        )
                        .border(
                            1.dp,
                            PortalGreen.copy(alpha = 0.35f),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = "♡",
                        color = PortalGreen,
                        fontSize = 48.sp
                    )
                }

                Spacer(
                    modifier = Modifier.height(22.dp)
                )

                Text(
                    text = "NO FAVORITES YET",
                    color = SpaceWhite,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = "Find a character you like and tap the heart\n" +
                            "to save them to your collection.",
                    color = SpaceWhite.copy(alpha = 0.65f),
                    fontSize = 12.sp,
                    lineHeight = 18.sp
                )
            }

            return
        }

        /*
         * FAVORITE LIST
         */
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 8.dp,
                bottom = 24.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            items(
                items = favorites,
                key = { it.id }
            ) { character ->

                FavoriteCharacterCard(
                    character = character,
                    onClick = {
                        onCharacterClick(character.id)
                    },
                    onRemove = {
                        viewModel.toggleFavorite(character)
                    }
                )
            }
        }
    }
}

@Composable
private fun FavoriteCharacterCard(
    character: FavoriteCharacter,
    onClick: () -> Unit,
    onRemove: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(GlassColor)
            .border(
                1.dp,
                Color.White.copy(alpha = 0.11f),
                RoundedCornerShape(22.dp)
            )
            .clickable(onClick = onClick)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        AsyncImage(
            model = character.image,
            contentDescription = character.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(78.dp)
                .clip(RoundedCornerShape(17.dp))
        )

        Spacer(
            modifier = Modifier.width(13.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = character.name,
                color = SpaceWhite,
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(
                modifier = Modifier.height(5.dp)
            )

            Text(
                text = "${character.species}  •  ${character.status}",
                color = SpaceWhite.copy(alpha = 0.65f),
                fontSize = 11.sp
            )

            Spacer(
                modifier = Modifier.height(7.dp)
            )

            Text(
                text = "CHARACTER #${character.id}",
                color = PortalMint,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }

        Box(
            modifier = Modifier
                .size(43.dp)
                .clip(CircleShape)
                .background(
                    PortalGreen.copy(alpha = 0.14f)
                )
                .border(
                    1.dp,
                    PortalGreen.copy(alpha = 0.38f),
                    CircleShape
                )
                .clickable(onClick = onRemove),
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = "♥",
                color = PortalGreen,
                fontSize = 21.sp
            )
        }
    }
}