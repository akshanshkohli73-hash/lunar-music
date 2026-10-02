package com.myapp.musicapp.ui.player

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.myapp.musicapp.player.PlayerConnection
import com.myapp.musicapp.ui.navigation.AppNavigation
import com.myapp.musicapp.ui.navigation.BottomNavBar
import com.myapp.musicapp.ui.screens.player.FullScreenPlayer
import com.myapp.musicapp.ui.screens.player.MiniPlayer
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerLayout() {
    val navController = rememberNavController()
    val playerConnection: PlayerConnection = koinInject()

    val currentSong by playerConnection.currentSong.collectAsStateWithLifecycle()
    val showMiniPlayer = currentSong != null

    val playerSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showFullPlayer by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            Column {
                AnimatedVisibility(
                    visible = showMiniPlayer,
                    enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                    exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
                ) {
                    MiniPlayer(
                        playerConnection = playerConnection,
                        onExpand = { showFullPlayer = true }
                    )
                }

                BottomNavBar(navController = navController)
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            AppNavigation(
                navController = navController,
                playerConnection = playerConnection
            )
        }
    }

    if (showFullPlayer) {
        ModalBottomSheet(
            onDismissRequest = { showFullPlayer = false },
            sheetState = playerSheetState,
            containerColor = Color.Transparent,
            dragHandle = null,
            windowInsets = WindowInsets(0)
        ) {
            FullScreenPlayer(
                playerConnection = playerConnection,
                onDismiss = { showFullPlayer = false },
                onExpandQueue = { }
            )
        }
    }
}
