package com.myapp.musicapp.data.repository

import com.myapp.musicapp.data.local.database.dao.SongDao
import com.myapp.musicapp.data.local.database.entities.toEntity
import com.myapp.musicapp.data.remote.innertube.InnertubeService
import com.myapp.musicapp.data.remote.piped.PipedApi
import com.myapp.musicapp.domain.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MusicRepository(
    private val innertubeService: InnertubeService,
    private val pipedApi: PipedApi,
    private val songDao: SongDao
) {
    suspend fun getStreamUrl(videoId: String): String {
        return try {
            innertubeService.getStreamUrl(videoId)
        } catch (e: Exception) {
            try {
                pipedApi.getAudioUrl(videoId)
            } catch (pipedEx: Exception) {
                throw e
            }
        }
    }

    fun getFavoriteSongs(): Flow<List<Song>> {
        return songDao.getFavoriteSongs().map { entities ->
            entities.map { it.toSong() }
        }
    }

    fun getRecentlyPlayed(): Flow<List<Song>> {
        return songDao.getRecentlyPlayed().map { entities ->
            entities.map { it.toSong() }
        }
    }

    suspend fun addToRecentlyPlayed(song: Song) {
        songDao.upsertSong(song.toEntity())
        songDao.updatePlayStats(song.id)
    }

    suspend fun toggleFavorite(songId: String) {
        songDao.toggleFavorite(songId)
    }

    suspend fun isFavorite(songId: String): Boolean {
        return songDao.getSongById(songId)?.isFavorite ?: false
    }

    suspend fun getQuickPicks(): List<Song> {
        return listOf(
            Song("vS3_72L-2O4", "Blinding Lights", "The Weeknd", "After Hours", "album_1", 200000, "https://i.ytimg.com/vi/vS3_72L-2O4/hqdefault.jpg"),
            Song("fJ9rUzIMcZQ", "Bohemian Rhapsody", "Queen", "A Night at the Opera", "album_2", 354000, "https://i.ytimg.com/vi/fJ9rUzIMcZQ/hqdefault.jpg"),
            Song("0V3w4iL12Q", "Levitating", "Dua Lipa", "Future Nostalgia", "album_3", 203000, "https://i.ytimg.com/vi/0V3w4iL12Q/hqdefault.jpg"),
            Song("JGwWNGJdvx8", "Shape of You", "Ed Sheeran", "÷", "album_4", 233000, "https://i.ytimg.com/vi/JGwWNGJdvx8/hqdefault.jpg"),
            Song("hT_nvWreIhg", "Counting Stars", "OneRepublic", "Native", "album_5", 257000, "https://i.ytimg.com/vi/hT_nvWreIhg/hqdefault.jpg")
        )
    }

    suspend fun getRecommendedAlbums(): List<Album> {
        return listOf(
            Album("MPREb_954848", "After Hours", "The Weeknd", "2020", "https://i.ytimg.com/vi/vS3_72L-2O4/hqdefault.jpg", 14),
            Album("MPREb_954849", "Future Nostalgia", "Dua Lipa", "2020", "https://i.ytimg.com/vi/0V3w4iL12Q/hqdefault.jpg", 11),
            Album("MPREb_954850", "Divide", "Ed Sheeran", "2017", "https://i.ytimg.com/vi/JGwWNGJdvx8/hqdefault.jpg", 16)
        )
    }

    suspend fun getTrendingArtists(): List<Artist> {
        return listOf(
            Artist("UC0WP5P-ufpKf5pUpfedNiQg", "The Weeknd", "https://i.ytimg.com/vi/vS3_72L-2O4/hqdefault.jpg", "35M subscribers"),
            Artist("UC1234567890", "Dua Lipa", "https://i.ytimg.com/vi/0V3w4iL12Q/hqdefault.jpg", "22M subscribers"),
            Artist("UC0000000001", "Ed Sheeran", "https://i.ytimg.com/vi/JGwWNGJdvx8/hqdefault.jpg", "50M subscribers")
        )
    }

    suspend fun getMoods(): List<Mood> {
        return listOf(
            Mood("Chill", "chill", "#4A90E2"),
            Mood("Workout", "workout", "#E74C3C"),
            Mood("Focus", "focus", "#2ECC71"),
            Mood("Party", "party", "#F1C40F"),
            Mood("Sleep", "sleep", "#9B59B6"),
            Mood("Romance", "romance", "#E91E63")
        )
    }
}
