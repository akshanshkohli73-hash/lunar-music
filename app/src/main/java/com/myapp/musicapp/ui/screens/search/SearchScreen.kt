package com.myapp.musicapp.ui.screens.search

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.myapp.musicapp.player.PlayerConnection
import com.myapp.musicapp.ui.components.ShimmerEffect
import com.myapp.musicapp.ui.components.SongListItem
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavHostController,
    playerConnection: PlayerConnection,
    viewModel: SearchViewModel = koinViewModel()
) {
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val searchResults by viewModel.searchResults.collectAsStateWithLifecycle()
    val searchHistory by viewModel.searchHistory.collectAsStateWithLifecycle()
    val suggestions by viewModel.suggestions.collectAsStateWithLifecycle()
    val isSearching by viewModel.isSearching.collectAsStateWithLifecycle()
    val selectedFilter by viewModel.selectedFilter.collectAsStateWithLifecycle()

    var isSearchBarActive by remember { mutableStateOf(false) }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            SearchBar(
                query = searchQuery,
                onQueryChange = viewModel::onQueryChange,
                onSearch = {
                    viewModel.search(it)
                    isSearchBarActive = false
                },
                active = isSearchBarActive,
                onActiveChange = { isSearchBarActive = it },
                placeholder = { Text("Search songs, artists, albums...") },
                leadingIcon = {
                    if (isSearchBarActive) {
                        IconButton(onClick = { isSearchBarActive = false }) {
                            Icon(Icons.AutoMirrored.Rounded.ArrowBack, "Back")
                        }
                    } else {
                        Icon(Icons.Rounded.Search, "Search")
                    }
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.onQueryChange("") }) {
                            Icon(Icons.Rounded.Close, "Clear")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = if (isSearchBarActive) 0.dp else 16.dp)
            ) {
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    if (suggestions.isNotEmpty()) {
                        items(suggestions) { suggestion ->
                            ListItem(
                                headlineContent = { Text(suggestion) },
                                leadingContent = { Icon(Icons.Rounded.TrendingUp, null) },
                                modifier = Modifier.clickable {
                                    viewModel.onQueryChange(suggestion)
                                    viewModel.search(suggestion)
                                    isSearchBarActive = false
                                }
                            )
                        }
                    }

                    if (searchQuery.isEmpty() && searchHistory.isNotEmpty()) {
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Recent searches", style = MaterialTheme.typography.titleSmall)
                                TextButton(onClick = { viewModel.clearHistory() }) {
                                    Text("Clear all")
                                }
                            }
                        }
                        items(searchHistory) { historyItem ->
                            ListItem(
                                headlineContent = { Text(historyItem.query) },
                                leadingContent = { Icon(Icons.Rounded.History, null) },
                                trailingContent = {
                                    IconButton(onClick = { viewModel.removeHistoryItem(historyItem) }) {
                                        Icon(Icons.Rounded.Close, "Remove")
                                    }
                                },
                                modifier = Modifier.clickable {
                                    viewModel.onQueryChange(historyItem.query)
                                    viewModel.search(historyItem.query)
                                    isSearchBarActive = false
                                }
                            )
                        }
                    }
                }
            }

            if (searchResults != null) {
                LazyRow(
                    modifier = Modifier.padding(vertical = 8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val filters = listOf("All", "Songs", "Albums", "Artists")
                    items(filters) { filter ->
                        FilterChip(
                            selected = selectedFilter == filter,
                            onClick = { viewModel.setFilter(filter) },
                            label = { Text(filter) }
                        )
                    }
                }
            }

            AnimatedContent(targetState = isSearching, label = "search_content") { searching ->
                if (searching) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        repeat(6) {
                            ShimmerEffect(modifier = Modifier.fillMaxWidth().height(56.dp))
                        }
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(bottom = 80.dp),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        searchResults?.let { results ->
                            if (results.songs.isNotEmpty() && (selectedFilter == "All" || selectedFilter == "Songs")) {
                                item {
                                    Text(
                                        "Songs",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(16.dp)
                                    )
                                }
                                items(results.songs, key = { it.id }) { song ->
                                    SongListItem(
                                        song = song,
                                        onClick = { playerConnection.playSong(song, results.songs) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
