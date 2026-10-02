package com.myapp.musicapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.myapp.musicapp.data.local.datastore.AppSettings
import com.myapp.musicapp.data.local.datastore.SettingsDataStore
import com.myapp.musicapp.data.local.datastore.ThemeMode
import com.myapp.musicapp.ui.player.PlayerLayout
import com.myapp.musicapp.ui.theme.MusicAppTheme
import org.koin.compose.koinInject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        installSplashScreen()

        setContent {
            val settingsDataStore: SettingsDataStore = koinInject()
            val settings by settingsDataStore.settingsFlow.collectAsStateWithLifecycle(
                initialValue = AppSettings()
            )

            MusicAppTheme(
                darkTheme = when (settings.themeMode) {
                    ThemeMode.LIGHT -> false
                    ThemeMode.DARK -> true
                    ThemeMode.SYSTEM -> isSystemInDarkTheme()
                },
                dynamicColor = settings.dynamicColor,
                pureBlack = settings.pureBlack
            ) {
                PlayerLayout()
            }
        }
    }
}
