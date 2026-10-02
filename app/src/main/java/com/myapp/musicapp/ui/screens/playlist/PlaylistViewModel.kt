package com.myapp.musicapp.ui.screens.playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myapp.musicapp.data.repository.MusicRepository
import com.myapp.musicapp.domain.model.Playlist
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val playlistId: String,
    private val musicRepository: MusicRepository
) : ViewModel() {

    private val _playlist = MutableStateFlow<Playlist?>(null)
    val playlist: StateFlow<Playlist?> = _playlist.asStateFlow()

    init {
        loadPlaylist()
    }

    private fun loadPlaylist() {
        viewModelScope.launch {
            _playlist.value = Playlist(
                id = playlistId,
                title = "My Playlist",
                author = "User",
                thumbnailUrl = "https://i.ytimg.com/vi/vS3_72L-2O4/hqdefault.jpg",
                songs = musicRepository.getQuickPicks()
            )
        }
    }
}
