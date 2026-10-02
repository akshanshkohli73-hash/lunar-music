package com.myapp.musicapp.data.remote.piped

import kotlinx.serialization.Serializable

@Serializable
data class PipedStreamInfo(
    val title: String = "",
    val description: String = "",
    val uploadDate: String = "",
    val uploader: String = "",
    val uploaderUrl: String = "",
    val uploaderAvatar: String = "",
    val thumbnailUrl: String = "",
    val audioStreams: List<PipedAudioStream> = emptyList(),
    val duration: Long = 0
)

@Serializable
data class PipedAudioStream(
    val url: String = "",
    val format: String = "",
    val quality: String = "",
    val mimeType: String = "",
    val bitrate: Long = 0
)
