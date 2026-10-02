package com.myapp.musicapp.domain.usecase

import com.myapp.musicapp.data.repository.MusicRepository
import com.myapp.musicapp.domain.model.*

class GetHomeContentUseCase(private val repository: MusicRepository) {
    suspend operator fun invoke(): HomeContent {
        return HomeContent(
            quickPicks = repository.getQuickPicks(),
            recommendedAlbums = repository.getRecommendedAlbums(),
            trendingArtists = repository.getTrendingArtists(),
            moods = repository.getMoods()
        )
    }
}

data class HomeContent(
    val quickPicks: List<Song>,
    val recommendedAlbums: List<Album>,
    val trendingArtists: List<Artist>,
    val moods: List<Mood>
)

class SearchMusicUseCase(private val repository: com.myapp.musicapp.data.repository.SearchRepository) {
    suspend operator fun invoke(query: String, filter: String? = null): SearchResult {
        return repository.search(query, filter)
    }
}

class GetSongStreamUseCase(private val repository: MusicRepository) {
    suspend operator fun invoke(songId: String): String {
        return repository.getStreamUrl(songId)
    }
}

class ManageLibraryUseCase(private val repository: com.myapp.musicapp.data.repository.LibraryRepository) {
    suspend fun createPlaylist(name: String) = repository.createPlaylist(name)
}

class GetRecommendationsUseCase(private val repository: MusicRepository) {
    suspend operator fun invoke(): List<Song> = repository.getQuickPicks()
}
