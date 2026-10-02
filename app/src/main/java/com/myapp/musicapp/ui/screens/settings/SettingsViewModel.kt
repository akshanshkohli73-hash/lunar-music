package com.myapp.musicapp.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myapp.musicapp.data.local.datastore.AppSettings
import com.myapp.musicapp.data.local.datastore.AudioQuality
import com.myapp.musicapp.data.local.datastore.SettingsDataStore
import com.myapp.musicapp.data.local.datastore.ThemeMode
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsDataStore: SettingsDataStore
) : ViewModel() {

    val settings: StateFlow<AppSettings> = settingsDataStore.settingsFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AppSettings()
    )

    fun setThemeMode(mode: ThemeMode) {
        viewModelScope.launch {
            settingsDataStore.setThemeMode(mode)
        }
    }

    fun setDynamicColor(enabled: Boolean) {
        viewModelScope.launch {
            settingsDataStore.setDynamicColor(enabled)
        }
    }

    fun setPureBlack(enabled: Boolean) {
        viewModelScope.launch {
            settingsDataStore.setPureBlack(enabled)
        }
    }

    fun setAlbumArtTheming(enabled: Boolean) {
        viewModelScope.launch {
            settingsDataStore.setAlbumArtTheming(enabled)
        }
    }

    fun setAudioQuality(quality: AudioQuality) {
        viewModelScope.launch {
            settingsDataStore.setAudioQuality(quality)
        }
    }

    fun setSkipSilence(enabled: Boolean) {
        viewModelScope.launch {
            settingsDataStore.setSkipSilence(enabled)
        }
    }

    fun clearCache() {
        // Clear cache logic
    }
}
