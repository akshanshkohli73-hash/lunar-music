# 🌙 Lunar Music - ViviMusic-Style Android Streaming App

**Lunar Music** is a modern, feature-rich Android music streaming application inspired by [ViviMusic](https://github.com/vivizzz007/vivi-music). It streams high-quality audio directly from YouTube Music (via Innertube API & Piped API) and features a Material Design 3 (M3) user interface with dynamic theming, smooth transitions, and background playback powered by Media3 ExoPlayer.

---

## 📱 Features

- **Material Design 3 & Dynamic Theming**: Full M3 color system support including wallpaper-based dynamic colors (Android 12+) and dynamic palette extraction from album artwork.
- **YouTube Music Streaming**: Live streaming of songs, search suggestions, albums, artists, and playlists using Innertube API and Piped fallback endpoints.
- **Background Media Playback**: Powered by Media3 ExoPlayer and `MediaSessionService` with background audio focus management and rich notification controls.
- **Local Library & Offline Storage**: Built with Room database and DataStore preferences to support playlists, favorite songs, search history, recently played tracks, and playback settings.
- **Interactive Full-Screen & Mini Player**:
  - Full-Screen Player with animated album artwork, real-time seek bar, animated favorite toggle, playback control options, and queue view.
  - persistent MiniPlayer with progress indicators and marquee track titles.
- **Comprehensive Navigation**: Single-activity architecture using Jetpack Compose Navigation for Home, Search, Library, Album Details, Artist Details, Playlist Details, and Settings screens.

---

## 🛠 Tech Stack & Architecture

| Component | Technology |
|---|---|
| **Language** | 100% Kotlin |
| **UI Framework** | Jetpack Compose (Material 3) |
| **Architecture** | MVVM + Clean Architecture |
| **Dependency Injection** | Koin |
| **Networking** | Ktor Client (OkHttp engine & Kotlinx Serialization) |
| **Local Database** | Room (SQLite) |
| **Data Storage** | Jetpack DataStore Preferences |
| **Media Playback** | androidx.media3:media3-exoplayer & media3-session |
| **Image Loading** | Coil Compose |
| **Navigation** | Compose Navigation (Single Activity) |
| **Min / Target SDK** | Min SDK 24 / Target SDK 34 |

---

## 📂 Project Structure

```
LunarMusic/
├── LunarMusic.apk                  # Compiled debug APK ready for installation
├── build.gradle.kts                # Root build script
├── settings.gradle.kts             # Settings configuration
├── gradle/libs.versions.toml       # Dependency version catalog
└── app/
    ├── build.gradle.kts            # Module build script
    └── src/main/
        ├── AndroidManifest.xml
        └── java/com/myapp/musicapp/
            ├── LunarMusicApp.kt    # Application class (Koin initialization)
            ├── MainActivity.kt     # Main entry point with edge-to-edge layout
            ├── di/
            │   └── AppModule.kt    # Koin DI modules for database, network, repo, & viewmodels
            ├── data/
            │   ├── local/
            │   │   ├── database/   # AppDatabase, Entities, DAOs, Converters
            │   │   └── datastore/  # SettingsDataStore (theme, quality, audio settings)
            │   ├── remote/
            │   │   ├── innertube/  # Innertube API client & response models
            │   │   └── piped/      # Piped API fallback integration
            │   └── repository/     # MusicRepository, SearchRepository, LibraryRepository
            ├── domain/
            │   ├── model/          # Song, Album, Artist, Playlist, Queue, Mood models
            │   └── usecase/        # Clean architecture use cases
            ├── player/
            │   ├── MusicService.kt # Media3 MediaSessionService implementation
            │   ├── PlayerConnection.kt # Reactive StateFlow bridge between UI & player
            │   └── notification/   # Notification manager configuration
            └── ui/
                ├── theme/          # M3 Theme, Typography, Shapes, Dynamic Color
                ├── navigation/     # AppNavigation, BottomNavBar, Route definitions
                ├── components/     # Reusable UI elements (SongListItem, AlbumCard, Shimmer)
                ├── player/         # PlayerLayout scaffold container
                └── screens/        # HomeScreen, SearchScreen, LibraryScreen, FullScreenPlayer, etc.
```

---

## 🚀 Building & Running

### Prerequisites
- JDK 17 or higher
- Android SDK 34
- Gradle 8.8 (wrapper included)

### Build Commands

To assemble the debug APK:
```bash
./gradlew assembleDebug
```
The compiled APK will be output to `app/build/outputs/apk/debug/app-debug.apk` and is also copied to `./LunarMusic.apk` in the repository root.

To assemble the release APK:
```bash
./gradlew assembleRelease
```

---

## 📄 Pre-Compiled APK Location

The pre-built APK for **Lunar Music** is included directly in the repository root:
- **`./LunarMusic.apk`**

You can install it directly on any Android device running Android 7.0 (API level 24) or higher using `adb`:
```bash
adb install -r LunarMusic.apk
```
