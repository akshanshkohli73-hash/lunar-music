package com.myapp.musicapp.ui.screens.artist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myapp.musicapp.data.repository.MusicRepository
import com.myapp.musicapp.domain.model.Artist
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ArtistDetailViewModel(
    private val artistId: String,
    private val musicRepository: MusicRepository
) : ViewModel() {

    private val _artist = MutableStateFlow<Artist?>(null)
    val artist: StateFlow<Artist?> = _artist.asStateFlow()

    init {
        loadArtist()
    }

    private fun loadArtist() {
        viewModelScope.launch {
            _artist.value = Artist(
                id = artistId,
                name = "Artist Name",
                thumbnailUrl = "https://i.ytimg.com/vi/vS3_72L-2O4/hqdefault.jpg",
                subscriberCount = "1.5M subscribers",
                topSongs = musicRepository.getQuickPicks(),
                albums = musicRepository.getRecommendedAlbums()
            )
        }
    }
}
