package com.myapp.musicapp.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Song(
    val id: String,
    val title: String,
    val artist: String,
    val album: String? = null,
    val albumId: String? = null,
    val duration: Long? = null,
    val thumbnailUrl: String? = null,
    val isFavorite: Boolean = false,
    val streamUrl: String? = null
)

@Serializable
data class Album(
    val id: String,
    val title: String,
    val artist: String,
    val year: String? = null,
    val thumbnailUrl: String? = null,
    val songCount: Int? = null,
    val songs: List<Song> = emptyList()
)

@Serializable
data class Artist(
    val id: String,
    val name: String,
    val thumbnailUrl: String? = null,
    val subscriberCount: String? = null,
    val topSongs: List<Song> = emptyList(),
    val albums: List<Album> = emptyList()
)

@Serializable
data class Playlist(
    val id: String,
    val title: String,
    val author: String? = null,
    val thumbnailUrl: String? = null,
    val songCount: Int? = null,
    val songs: List<Song> = emptyList()
)

@Serializable
data class SearchResult(
    val songs: List<Song> = emptyList(),
    val albums: List<Album> = emptyList(),
    val artists: List<Artist> = emptyList(),
    val playlists: List<Playlist> = emptyList()
)

@Serializable
data class Queue(
    val title: String = "Queue",
    val items: List<Song> = emptyList(),
    val currentIndex: Int = 0
)

@Serializable
data class Mood(
    val title: String,
    val params: String,
    val colorHex: String? = null
)

data class DynamicHomeSection(
    val title: String,
    val subtitle: String? = null,
    val songs: List<Song> = emptyList(),
    val albums: List<Album> = emptyList(),
    val playlists: List<Playlist> = emptyList()
)
