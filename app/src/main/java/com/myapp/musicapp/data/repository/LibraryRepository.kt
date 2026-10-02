package com.myapp.musicapp.data.repository

import com.myapp.musicapp.data.local.database.dao.PlaylistDao
import com.myapp.musicapp.data.local.database.dao.SongDao
import com.myapp.musicapp.data.local.database.entities.PlaylistEntity
import com.myapp.musicapp.data.local.database.entities.PlaylistSongCrossRef
import com.myapp.musicapp.data.local.database.entities.toEntity
import com.myapp.musicapp.domain.model.Playlist
import com.myapp.musicapp.domain.model.Song
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LibraryRepository(
    private val playlistDao: PlaylistDao,
    private val songDao: SongDao
) {
    fun getAllPlaylists(): Flow<List<Playlist>> {
        return playlistDao.getAllPlaylists().map { entities ->
            entities.map { entity ->
                Playlist(
                    id = entity.id.toString(),
                    title = entity.name,
                    thumbnailUrl = entity.thumbnailUrl
                )
            }
        }
    }

    suspend fun createPlaylist(name: String, description: String? = null): Long {
        return playlistDao.insertPlaylist(
            PlaylistEntity(name = name, description = description)
        )
    }

    suspend fun addSongToPlaylist(playlistId: Long, song: Song) {
        songDao.upsertSong(song.toEntity())
        playlistDao.insertPlaylistSongCrossRef(
            PlaylistSongCrossRef(
                playlistId = playlistId,
                songId = song.id,
                position = 0
            )
        )
    }

    suspend fun removeSongFromPlaylist(playlistId: Long, songId: String) {
        playlistDao.removeSongFromPlaylist(playlistId, songId)
    }

    suspend fun deletePlaylist(playlistId: Long) {
        playlistDao.deletePlaylist(PlaylistEntity(id = playlistId, name = ""))
    }
}
