package com.myapp.musicapp.data.remote.innertube.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class InnertubeResponse(
    val contents: JsonObject? = null
)

@Serializable
data class BrowseResponse(
    val contents: JsonObject? = null,
    val header: JsonObject? = null
)

@Serializable
data class SearchResponse(
    val contents: JsonObject? = null
)

@Serializable
data class PlayerResponse(
    val streamingData: StreamingData? = null,
    val videoDetails: VideoDetails? = null
)

@Serializable
data class StreamingData(
    val adaptiveFormats: List<AdaptiveFormat> = emptyList()
)

@Serializable
data class AdaptiveFormat(
    val itag: Int = 0,
    val url: String? = null,
    val mimeType: String = "",
    val bitrate: Long = 0,
    val contentLength: String? = null,
    val audioQuality: String? = null
)

@Serializable
data class VideoDetails(
    val videoId: String = "",
    val title: String = "",
    val author: String = "",
    val lengthSeconds: String = "0",
    val thumbnail: ThumbnailContainer? = null
)

@Serializable
data class ThumbnailContainer(
    val thumbnails: List<Thumbnail> = emptyList()
)

@Serializable
data class Thumbnail(
    val url: String = "",
    val width: Int = 0,
    val height: Int = 0
)

@Serializable
data class NextResponse(
    val contents: JsonObject? = null
)

@Serializable
data class SuggestionsResponse(
    val suggestions: List<String> = emptyList()
)
