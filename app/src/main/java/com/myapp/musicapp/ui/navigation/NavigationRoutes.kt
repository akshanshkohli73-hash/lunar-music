package com.myapp.musicapp.ui.navigation

sealed class Route(val path: String) {
    object Home : Route("home")
    object Search : Route("search")
    object Library : Route("library")
    object Settings : Route("settings")
    object Player : Route("player")
    data class AlbumDetail(val id: String) : Route("album/{id}")
    data class ArtistDetail(val id: String) : Route("artist/{id}")
    data class PlaylistDetail(val id: String) : Route("playlist/{id}")
    data class Mood(val params: String) : Route("mood/{params}")
}
