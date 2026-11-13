package com.example.projectandroid.ui.screens.home

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projectandroid.R

private val SpaceBlack = Color(0xFF080D18)
private val PortalGreen = Color(0xFFB7FF4A)
private val PortalMint = Color(0xFF78F5B2)
private val SpaceWhite = Color(0xFFF7F9FF)
private val GlassColor = Color(0xE9101928)

@Composable
fun HomeScreen(
    onNavigateToCategories: () -> Unit = {},
    onNavigateToEpisodes: () -> Unit = {},
    onNavigateToFavorites: () -> Unit = {}
) {

    var selectedTab by remember { mutableStateOf("Home") }
    var portalActivated by remember { mutableStateOf(false) }

    // ---------------------------------------------------------
    // ANIMATIONS
    // ---------------------------------------------------------

    val transition = rememberInfiniteTransition(
        label = "homeAnimations"
    )

    // Pulsing green light
    val pulse by transition.animateFloat(
        initialValue = 0.45f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1300,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "portalGlow"
    )

    // Floating portal button
    val floatY by transition.animateFloat(
        initialValue = -9f,
        targetValue = 9f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1400,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "floatingPortalButton"
    )

    Scaffold(
        containerColor = SpaceBlack,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            // ---------------------------------------------------------
            // 1. FULL-SCREEN WALLPAPER
            // ---------------------------------------------------------

            Image(
                painter = painterResource(
                    id = R.drawable.rick_morty_wallpaper
                ),
                contentDescription = "Rick and Morty multiverse wallpaper",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // ---------------------------------------------------------
            // 2. CINEMATIC DARK OVERLAY
            // ---------------------------------------------------------

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colorStops = arrayOf(
                                0.0f to Color.Black.copy(alpha = 0.48f),
                                0.18f to Color.Transparent,
                                0.42f to Color.Transparent,
                                0.62f to Color.Black.copy(alpha = 0.25f),
                                1.0f to Color(0xFF080D18)
                                    .copy(alpha = 0.98f)
                            )
                        )
                    )
            )

            // ---------------------------------------------------------
            // 3. FLOATING PORTAL BUTTON
            // ---------------------------------------------------------

            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 12.dp)
                    .offset(y = floatY.dp)
                    .size(58.dp)
                    .shadow(
                        elevation = 14.dp,
                        shape = CircleShape,
                        clip = false
                    )
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                Color(0xFFD4FF83),
                                PortalGreen,
                                PortalMint
                            )
                        )
                    )
                    .border(
                        width = 2.dp,
                        color = Color.White.copy(alpha = 0.8f),
                        shape = CircleShape
                    )
                    .clickable {
                        portalActivated = !portalActivated
                    },
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "✦",
                    color = Color(0xFF102016),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Black
                )
            }

            // ---------------------------------------------------------
            // 4. DIMENSION BADGE
            // ---------------------------------------------------------

            Row(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(
                        top = WindowInsets.statusBars
                            .asPaddingValues()
                            .calculateTopPadding() + 12.dp
                    )
                    .clip(CircleShape)
                    .background(Color(0xD9101928))
                    .border(
                        width = 1.dp,
                        color = PortalGreen.copy(alpha = 0.38f),
                        shape = CircleShape
                    )
                    .padding(
                        horizontal = 15.dp,
                        vertical = 10.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .shadow(
                            elevation = 7.dp,
                            shape = CircleShape,
                            clip = false
                        )
                        .clip(CircleShape)
                        .background(
                            PortalGreen.copy(alpha = pulse)
                        )
                )

                Text(
                    text = "  DIMENSION C-137",
                    color = SpaceWhite,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.8.sp
                )

                Text(
                    text = "  ✦",
                    color = PortalGreen,
                    fontSize = 13.sp
                )
            }

            // ---------------------------------------------------------
            // 5. BOTTOM CONTENT
            // ---------------------------------------------------------

            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        bottom = WindowInsets.navigationBars
                            .asPaddingValues()
                            .calculateBottomPadding() + 12.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // -----------------------------------------------------
                // GLASS WELCOME CARD
                // -----------------------------------------------------

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(28.dp))
                        .background(GlassColor)
                        .border(
                            width = 1.dp,
                            color = Color.White.copy(alpha = 0.16f),
                            shape = RoundedCornerShape(28.dp)
                        )
                        .padding(
                            horizontal = 20.dp,
                            vertical = 19.dp
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "YOUR MULTIVERSE PASS",
                        color = PortalMint,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.2.sp
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        text = "EVERY WORLD HAS A STORY.",
                        color = SpaceWhite,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        textAlign = TextAlign.Center,
                        lineHeight = 28.sp
                    )

                    Spacer(
                        modifier = Modifier.height(7.dp)
                    )

                    Text(
                        text = if (portalActivated) {
                            "Your adventure is ready. Let's explore!"
                        } else {
                            "Meet the characters. Discover their universe."
                        },
                        color = SpaceWhite.copy(alpha = 0.76f),
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(
                        modifier = Modifier.height(17.dp)
                    )

                    // -------------------------------------------------
                    // START EXPLORING BUTTON
                    // -------------------------------------------------

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp)
                            .shadow(
                                elevation = 10.dp,
                                shape = RoundedCornerShape(17.dp),
                                clip = false
                            )
                            .clip(RoundedCornerShape(17.dp))
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0xFFD1FF81),
                                        PortalGreen,
                                        PortalMint
                                    )
                                )
                            )
                            .clickable {
                                portalActivated = true
                                selectedTab = "Characters"
                                onNavigateToCategories()
                            },
                        contentAlignment = Alignment.Center
                    ) {

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {

                            Text(
                                text = if (portalActivated) {
                                    "PORTAL ACTIVATED"
                                } else {
                                    "START EXPLORING"
                                },
                                color = Color(0xFF102016),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 1.1.sp
                            )

                            Spacer(
                                modifier = Modifier.size(10.dp)
                            )

                            Text(
                                text = "↗",
                                color = Color(0xFF102016),
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                // -----------------------------------------------------
                // BOTTOM NAVIGATION
                // -----------------------------------------------------

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color(0xEE0C1422))
                        .border(
                            width = 1.dp,
                            color = Color.White.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(24.dp)
                        )
                        .padding(
                            horizontal = 5.dp,
                            vertical = 6.dp
                        ),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // HOME

                    NavigationItem(
                        icon = "⌂",
                        label = "Home",
                        selected = selectedTab == "Home",
                        onClick = {
                            selectedTab = "Home"
                            portalActivated = false
                        },
                        modifier = Modifier.weight(1f)
                    )

                    // CHARACTERS

                    NavigationItem(
                        icon = "✧",
                        label = "Characters",
                        selected = selectedTab == "Characters",
                        onClick = {
                            selectedTab = "Characters"
                            onNavigateToCategories()
                        },
                        modifier = Modifier.weight(1f)
                    )

                    // FAVORITES
                    // The text variation selector (︎) prevents
                    // Android from rendering the heart as a red emoji.

                    NavigationItem(
                        icon = "♥︎",
                        label = "Favorites",
                        selected = selectedTab == "Favorites",
                        onClick = {
                            selectedTab = "Favorites"
                            onNavigateToFavorites()
                        },
                        modifier = Modifier.weight(1f)
                    )

                    // EPISODES

                    NavigationItem(
                        icon = "▷",
                        label = "Episodes",
                        selected = selectedTab == "Episodes",
                        onClick = {
                            selectedTab = "Episodes"
                            onNavigateToEpisodes()
                        },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = "WUBBA LUBBA DUB DUB!  •  EXPLORE THE UNKNOWN",
                    color = SpaceWhite.copy(alpha = 0.65f),
                    fontSize = 8.sp,
                    letterSpacing = 0.8.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

// =============================================================
// BOTTOM NAVIGATION ITEM
// =============================================================

@Composable
fun NavigationItem(
    icon: String,
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(
                if (selected) {
                    PortalGreen.copy(alpha = 0.13f)
                } else {
                    Color.Transparent
                }
            )
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = icon,
            color = if (selected) {
                PortalGreen
            } else {
                SpaceWhite.copy(alpha = 0.8f)
            },
            fontSize = 23.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(3.dp)
        )

        Text(
            text = label,
            color = if (selected) {
                PortalGreen
            } else {
                SpaceWhite.copy(alpha = 0.75f)
            },
            fontSize = 10.sp,
            fontWeight = if (selected) {
                FontWeight.Bold
            } else {
                FontWeight.Medium
            }
        )
    }
}