package com.myapp.musicapp.ui.screens.library

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Sort
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.myapp.musicapp.domain.model.Playlist
import com.myapp.musicapp.domain.model.Song
import com.myapp.musicapp.player.PlayerConnection
import com.myapp.musicapp.ui.components.SongListItem
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    navController: NavHostController,
    playerConnection: PlayerConnection,
    viewModel: LibraryViewModel = koinViewModel()
) {
    val playlists by viewModel.playlists.collectAsStateWithLifecycle()
    val favoriteSongs by viewModel.favoriteSongs.collectAsStateWithLifecycle()
    val downloadedSongs by viewModel.downloadedSongs.collectAsStateWithLifecycle()
    val selectedTab by viewModel.selectedTab.collectAsStateWithLifecycle()

    var showCreatePlaylistDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Your Library",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = { showCreatePlaylistDialog = true }) {
                        Icon(Icons.Rounded.Add, "Create Playlist")
                    }
                    IconButton(onClick = { }) {
                        Icon(Icons.AutoMirrored.Rounded.Sort, "Sort")
                    }
                }
            )
        },
        floatingActionButton = {
            if (selectedTab == 1 && favoriteSongs.isNotEmpty()) {
                ExtendedFloatingActionButton(
                    onClick = {
                        playerConnection.playSong(favoriteSongs.random(), favoriteSongs.shuffled())
                    },
                    icon = { Icon(Icons.Rounded.Shuffle, "Shuffle") },
                    text = { Text("Shuffle All") },
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onSurface,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier
                            .tabIndicatorOffset(tabPositions[selectedTab])
                            .clip(RoundedCornerShape(topStart = 3.dp, topEnd = 3.dp)),
                        height = 3.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { viewModel.selectTab(0) },
                    text = { Text("Playlists") },
                    icon = { Icon(Icons.Rounded.QueueMusic, null) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { viewModel.selectTab(1) },
                    text = { Text("Favorites") },
                    icon = { Icon(Icons.Rounded.Favorite, null) }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { viewModel.selectTab(2) },
                    text = { Text("Downloads") },
                    icon = { Icon(Icons.Rounded.Download, null) }
                )
            }

            AnimatedContent(targetState = selectedTab, label = "library_tabs") { tab ->
                when (tab) {
                    0 -> PlaylistsTab(playlists, navController)
                    1 -> FavoritesTab(favoriteSongs, playerConnection)
                    2 -> DownloadsTab(downloadedSongs, playerConnection)
                }
            }
        }
    }

    if (showCreatePlaylistDialog) {
        CreatePlaylistDialog(
            onDismiss = { showCreatePlaylistDialog = false },
            onCreate = { name ->
                viewModel.createPlaylist(name)
                showCreatePlaylistDialog = false
            }
        )
    }
}

@Composable
fun PlaylistsTab(playlists: List<Playlist>, navController: NavHostController) {
    if (playlists.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No playlists created yet.")
        }
    } else {
        LazyColumn(contentPadding = PaddingValues(bottom = 80.dp)) {
            items(playlists, key = { it.id }) { playlist ->
                ListItem(
                    headlineContent = { Text(playlist.title) },
                    leadingContent = { Icon(Icons.Rounded.QueueMusic, null) },
                    modifier = Modifier.clickable { navController.navigate("playlist/${playlist.id}") }
                )
            }
        }
    }
}

@Composable
fun FavoritesTab(favoriteSongs: List<Song>, playerConnection: PlayerConnection) {
    if (favoriteSongs.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No favorite songs yet.")
        }
    } else {
        LazyColumn(contentPadding = PaddingValues(bottom = 80.dp)) {
            items(favoriteSongs, key = { it.id }) { song ->
                SongListItem(
                    song = song,
                    onClick = { playerConnection.playSong(song, favoriteSongs) }
                )
            }
        }
    }
}

@Composable
fun DownloadsTab(downloadedSongs: List<Song>, playerConnection: PlayerConnection) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("No downloads found.")
    }
}

@Composable
fun CreatePlaylistDialog(onDismiss: () -> Unit, onCreate: (String) -> Unit) {
    var name by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create Playlist") },
        text = {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Playlist Name") },
                singleLine = true
            )
        },
        confirmButton = {
            TextButton(
                onClick = { if (name.isNotBlank()) onCreate(name) },
                enabled = name.isNotBlank()
            ) {
                Text("Create")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
