package com.myapp.musicapp.ui.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.myapp.musicapp.BuildConfig
import com.myapp.musicapp.data.local.datastore.ThemeMode
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavHostController,
    viewModel: SettingsViewModel = koinViewModel()
) {
    val settings by viewModel.settings.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            item {
                SettingsCategory(title = "Appearance") {
                    SettingsListItem(
                        title = "Theme",
                        subtitle = settings.themeMode.displayName,
                        icon = Icons.Rounded.Palette,
                        onClick = {
                            val nextMode = when (settings.themeMode) {
                                ThemeMode.SYSTEM -> ThemeMode.DARK
                                ThemeMode.DARK -> ThemeMode.LIGHT
                                ThemeMode.LIGHT -> ThemeMode.SYSTEM
                            }
                            viewModel.setThemeMode(nextMode)
                        }
                    )

                    SettingsSwitchItem(
                        title = "Dynamic Colors",
                        subtitle = "Use wallpaper colors for theming",
                        icon = Icons.Rounded.ColorLens,
                        checked = settings.dynamicColor,
                        onCheckedChange = { viewModel.setDynamicColor(it) }
                    )

                    SettingsSwitchItem(
                        title = "Pure Black",
                        subtitle = "AMOLED-friendly dark theme",
                        icon = Icons.Rounded.DarkMode,
                        checked = settings.pureBlack,
                        onCheckedChange = { viewModel.setPureBlack(it) },
                        enabled = settings.themeMode != ThemeMode.LIGHT
                    )

                    SettingsSwitchItem(
                        title = "Album Art Colors",
                        subtitle = "Theme player based on album artwork",
                        icon = Icons.Rounded.Image,
                        checked = settings.albumArtTheming,
                        onCheckedChange = { viewModel.setAlbumArtTheming(it) }
                    )
                }
            }

            item {
                SettingsCategory(title = "Playback") {
                    SettingsListItem(
                        title = "Audio Quality",
                        subtitle = settings.audioQuality.displayName,
                        icon = Icons.Rounded.HighQuality,
                        onClick = { }
                    )

                    SettingsSwitchItem(
                        title = "Skip Silence",
                        subtitle = "Automatically skip silent parts",
                        icon = Icons.Rounded.Speed,
                        checked = settings.skipSilence,
                        onCheckedChange = { viewModel.setSkipSilence(it) }
                    )
                }
            }

            item {
                SettingsCategory(title = "Storage & Data") {
                    SettingsListItem(
                        title = "Cache Size",
                        subtitle = settings.cacheSize,
                        icon = Icons.Rounded.Storage,
                        onClick = { }
                    )

                    SettingsListItem(
                        title = "Clear Cache",
                        subtitle = "Free up storage space",
                        icon = Icons.Rounded.DeleteSweep,
                        onClick = { viewModel.clearCache() }
                    )
                }
            }

            item {
                SettingsCategory(title = "About") {
                    SettingsListItem(
                        title = "Version",
                        subtitle = BuildConfig.VERSION_NAME,
                        icon = Icons.Rounded.Info
                    )
                    SettingsListItem(
                        title = "Lunar Music",
                        subtitle = "Modern YouTube Music streaming app",
                        icon = Icons.Rounded.Code,
                        onClick = { }
                    )
                }
            }
        }
    }
}

@Composable
fun SettingsCategory(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        content()
    }
}

@Composable
fun SettingsListItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: (() -> Unit)? = null
) {
    ListItem(
        headlineContent = { Text(title) },
        supportingContent = { Text(subtitle) },
        leadingContent = { Icon(icon, contentDescription = null) },
        modifier = if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier
    )
}

@Composable
fun SettingsSwitchItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true
) {
    ListItem(
        headlineContent = { Text(title) },
        supportingContent = { Text(subtitle) },
        leadingContent = { Icon(icon, contentDescription = null) },
        trailingContent = {
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                enabled = enabled
            )
        }
    )
}
