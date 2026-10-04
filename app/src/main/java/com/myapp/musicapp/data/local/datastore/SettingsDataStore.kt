package com.myapp.musicapp.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

enum class ThemeMode(val displayName: String) {
    SYSTEM("System Default"),
    LIGHT("Light"),
    DARK("Dark")
}

enum class AudioQuality(val displayName: String) {
    LOW("Low (96kbps)"),
    MEDIUM("Medium (160kbps)"),
    HIGH("High (256kbps)")
}

data class AppSettings(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val dynamicColor: Boolean = true,
    val pureBlack: Boolean = false,
    val albumArtTheming: Boolean = true,
    val audioQuality: AudioQuality = AudioQuality.HIGH,
    val skipSilence: Boolean = false,
    val cacheSize: String = "512 MB"
)

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsDataStore(private val context: Context) {

    private object Keys {
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val DYNAMIC_COLOR = booleanPreferencesKey("dynamic_color")
        val PURE_BLACK = booleanPreferencesKey("pure_black")
        val ALBUM_ART_THEMING = booleanPreferencesKey("album_art_theming")
        val AUDIO_QUALITY = stringPreferencesKey("audio_quality")
        val SKIP_SILENCE = booleanPreferencesKey("skip_silence")
    }

    val settingsFlow: Flow<AppSettings> = context.dataStore.data.map { preferences ->
        val themeModeString = preferences[Keys.THEME_MODE]
        val themeMode = try {
            if (themeModeString != null) ThemeMode.valueOf(themeModeString) else ThemeMode.SYSTEM
        } catch (e: Exception) {
            ThemeMode.SYSTEM
        }

        val audioQualityString = preferences[Keys.AUDIO_QUALITY]
        val audioQuality = try {
            if (audioQualityString != null) AudioQuality.valueOf(audioQualityString) else AudioQuality.HIGH
        } catch (e: Exception) {
            AudioQuality.HIGH
        }

        AppSettings(
            themeMode = themeMode,
            dynamicColor = preferences[Keys.DYNAMIC_COLOR] ?: true,
            pureBlack = preferences[Keys.PURE_BLACK] ?: false,
            albumArtTheming = preferences[Keys.ALBUM_ART_THEMING] ?: true,
            audioQuality = audioQuality,
            skipSilence = preferences[Keys.SKIP_SILENCE] ?: false
        )
    }

    suspend fun setThemeMode(mode: ThemeMode) {
        context.dataStore.edit { preferences ->
            preferences[Keys.THEME_MODE] = mode.name
        }
    }

    suspend fun setDynamicColor(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[Keys.DYNAMIC_COLOR] = enabled
        }
    }

    suspend fun setPureBlack(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[Keys.PURE_BLACK] = enabled
        }
    }

    suspend fun setAlbumArtTheming(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[Keys.ALBUM_ART_THEMING] = enabled
        }
    }

    suspend fun setAudioQuality(quality: AudioQuality) {
        context.dataStore.edit { preferences ->
            preferences[Keys.AUDIO_QUALITY] = quality.name
        }
    }

    suspend fun setSkipSilence(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[Keys.SKIP_SILENCE] = enabled
        }
    }
}
