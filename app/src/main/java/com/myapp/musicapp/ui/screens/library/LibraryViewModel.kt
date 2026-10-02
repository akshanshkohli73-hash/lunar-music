package com.myapp.musicapp.ui.screens.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myapp.musicapp.data.repository.LibraryRepository
import com.myapp.musicapp.data.repository.MusicRepository
import com.myapp.musicapp.domain.model.Playlist
import com.myapp.musicapp.domain.model.Song
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LibraryViewModel(
    private val libraryRepository: LibraryRepository,
    private val musicRepository: MusicRepository
) : ViewModel() {

    private val _selectedTab = MutableStateFlow(0)
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    val playlists: StateFlow<List<Playlist>> = libraryRepository.getAllPlaylists().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val favoriteSongs: StateFlow<List<Song>> = musicRepository.getFavoriteSongs().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val downloadedSongs: StateFlow<List<Song>> = MutableStateFlow<List<Song>>(emptyList()).asStateFlow()

    fun selectTab(index: Int) {
        _selectedTab.value = index
    }

    fun createPlaylist(name: String) {
        viewModelScope.launch {
            libraryRepository.createPlaylist(name)
        }
    }
}
