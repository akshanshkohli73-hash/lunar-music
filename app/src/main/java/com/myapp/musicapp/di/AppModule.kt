package com.myapp.musicapp.di

import androidx.room.Room
import com.myapp.musicapp.data.local.database.AppDatabase
import com.myapp.musicapp.data.local.datastore.SettingsDataStore
import com.myapp.musicapp.data.remote.innertube.InnertubeService
import com.myapp.musicapp.data.remote.piped.PipedApi
import com.myapp.musicapp.data.repository.LibraryRepository
import com.myapp.musicapp.data.repository.MusicRepository
import com.myapp.musicapp.data.repository.SearchRepository
import com.myapp.musicapp.player.PlayerConnection
import com.myapp.musicapp.ui.screens.album.AlbumDetailViewModel
import com.myapp.musicapp.ui.screens.artist.ArtistDetailViewModel
import com.myapp.musicapp.ui.screens.home.HomeViewModel
import com.myapp.musicapp.ui.screens.library.LibraryViewModel
import com.myapp.musicapp.ui.screens.player.PlayerViewModel
import com.myapp.musicapp.ui.screens.playlist.PlaylistViewModel
import com.myapp.musicapp.ui.screens.search.SearchViewModel
import com.myapp.musicapp.ui.screens.settings.SettingsViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

val appModule = module {
    single { SettingsDataStore(get()) }

    single {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java,
            "music_app_database"
        )
        .fallbackToDestructiveMigration()
        .build()
    }
    single { get<AppDatabase>().songDao() }
    single { get<AppDatabase>().albumDao() }
    single { get<AppDatabase>().artistDao() }
    single { get<AppDatabase>().playlistDao() }
    single { get<AppDatabase>().searchHistoryDao() }

    single {
        HttpClient(OkHttp) {
            install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true; isLenient = true }) }
            install(Logging) { level = LogLevel.NONE }
            defaultRequest {
                header("User-Agent", "Mozilla/5.0")
                header("Referer", "https://music.youtube.com/")
            }
            engine {
                config {
                    connectTimeout(15, TimeUnit.SECONDS)
                    readTimeout(30, TimeUnit.SECONDS)
                }
            }
        }
    }

    single { InnertubeService(get()) }
    single { PipedApi(get()) }

    single { MusicRepository(get(), get(), get()) }
    single { SearchRepository(get(), get()) }
    single { LibraryRepository(get(), get()) }

    single { PlayerConnection(get(), get()) }

    viewModel { HomeViewModel(get()) }
    viewModel { SearchViewModel(get()) }
    viewModel { LibraryViewModel(get(), get()) }
    viewModel { PlayerViewModel(get()) }
    viewModel { SettingsViewModel(get()) }
    viewModel { (albumId: String) -> AlbumDetailViewModel(albumId, get()) }
    viewModel { (artistId: String) -> ArtistDetailViewModel(artistId, get()) }
    viewModel { (playlistId: String) -> PlaylistViewModel(playlistId, get()) }
}
