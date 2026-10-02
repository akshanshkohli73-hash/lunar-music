package com.myapp.musicapp.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myapp.musicapp.data.repository.MusicRepository
import com.myapp.musicapp.domain.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(
        val quickPicks: List<Song>,
        val recentlyPlayed: List<Song>,
        val recommendedAlbums: List<Album>,
        val trendingArtists: List<Artist>,
        val moods: List<Mood>,
        val featuredPlaylists: List<Playlist> = emptyList(),
        val dynamicSections: List<DynamicHomeSection> = emptyList()
    ) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}

class HomeViewModel(
    private val musicRepository: MusicRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadHomeData()
    }

    fun refresh() {
        loadHomeData()
    }

    private fun loadHomeData() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            try {
                val quickPicks = musicRepository.getQuickPicks()
                val albums = musicRepository.getRecommendedAlbums()
                val artists = musicRepository.getTrendingArtists()
                val moods = musicRepository.getMoods()

                _uiState.value = HomeUiState.Success(
                    quickPicks = quickPicks,
                    recentlyPlayed = quickPicks.take(3),
                    recommendedAlbums = albums,
                    trendingArtists = artists,
                    moods = moods
                )
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error(e.message ?: "Failed to load content")
            }
        }
    }
}
