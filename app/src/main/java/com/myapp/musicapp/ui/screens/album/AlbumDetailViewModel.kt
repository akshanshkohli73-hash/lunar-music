package com.myapp.musicapp.ui.screens.album

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myapp.musicapp.data.repository.MusicRepository
import com.myapp.musicapp.domain.model.Album
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AlbumDetailViewModel(
    private val albumId: String,
    private val musicRepository: MusicRepository
) : ViewModel() {

    private val _album = MutableStateFlow<Album?>(null)
    val album: StateFlow<Album?> = _album.asStateFlow()

    init {
        loadAlbum()
    }

    private fun loadAlbum() {
        viewModelScope.launch {
            _album.value = Album(
                id = albumId,
                title = "Album Title",
                artist = "Artist Name",
                year = "2024",
                thumbnailUrl = "https://i.ytimg.com/vi/vS3_72L-2O4/hqdefault.jpg",
                songs = musicRepository.getQuickPicks()
            )
        }
    }
}
