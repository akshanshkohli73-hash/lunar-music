package com.myapp.musicapp.data.remote.innertube

import com.myapp.musicapp.data.remote.innertube.models.*
import com.myapp.musicapp.data.remote.innertube.utils.InnertubeContext
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonObject

class InnertubeService(private val client: HttpClient) {

    companion object {
        private const val BASE_URL = "https://music.youtube.com/youtubei/v1"
        private const val API_KEY = "AIzaSyAO_FJ2SlqU8Q4STEHLGCilw_Y9_11qcW8"
    }

    private val desktopContext = InnertubeContext(
        client = InnertubeContext.Client(
            clientName = "WEB_REMIX",
            clientVersion = "1.20231204.01.00",
            platform = "DESKTOP",
            hl = "en",
            gl = "US"
        )
    )

    private val androidContext = InnertubeContext(
        client = InnertubeContext.Client(
            clientName = "ANDROID_MUSIC",
            clientVersion = "6.42.52",
            platform = "MOBILE",
            androidSdkVersion = 30
        )
    )

    suspend fun browse(browseId: String): BrowseResponse {
        return client.post("$BASE_URL/browse") {
            parameter("key", API_KEY)
            contentType(ContentType.Application.Json)
            setBody(buildJsonObject {
                put("context", desktopContext.toJsonObject())
                put("browseId", browseId)
            })
        }.body()
    }

    suspend fun search(query: String, filter: String? = null): SearchResponse {
        return client.post("$BASE_URL/search") {
            parameter("key", API_KEY)
            contentType(ContentType.Application.Json)
            setBody(buildJsonObject {
                put("context", desktopContext.toJsonObject())
                put("query", query)
                if (filter != null) put("params", filter)
            })
        }.body()
    }

    suspend fun getSearchSuggestions(query: String): List<String> {
        return try {
            val response: SuggestionsResponse = client.post("$BASE_URL/music/get_search_suggestions") {
                parameter("key", API_KEY)
                contentType(ContentType.Application.Json)
                setBody(buildJsonObject {
                    put("context", desktopContext.toJsonObject())
                    put("input", query)
                })
            }.body()
            response.suggestions
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun player(videoId: String): PlayerResponse {
        return client.post("$BASE_URL/player") {
            parameter("key", API_KEY)
            contentType(ContentType.Application.Json)
            setBody(buildJsonObject {
                put("context", androidContext.toJsonObject())
                put("videoId", videoId)
                putJsonObject("playbackContext") {
                    putJsonObject("contentPlaybackContext") {
                        put("signatureTimestamp", 19700)
                    }
                }
            })
        }.body()
    }

    suspend fun getStreamUrl(videoId: String): String {
        val playerResponse = player(videoId)
        return playerResponse.streamingData
            ?.adaptiveFormats
            ?.filter { it.mimeType.startsWith("audio/") }
            ?.maxByOrNull { it.bitrate }
            ?.url
            ?: throw Exception("No stream URL found for videoId: $videoId")
    }

    suspend fun next(videoId: String, playlistId: String? = null): NextResponse {
        return client.post("$BASE_URL/next") {
            parameter("key", API_KEY)
            contentType(ContentType.Application.Json)
            setBody(buildJsonObject {
                put("context", desktopContext.toJsonObject())
                put("videoId", videoId)
                if (playlistId != null) put("playlistId", playlistId)
                put("isAudioOnly", true)
            })
        }.body()
    }

    suspend fun getHomePage(): BrowseResponse = browse("FEmusic_home")
    suspend fun getAlbum(browseId: String): BrowseResponse = browse(browseId)
    suspend fun getArtist(browseId: String): BrowseResponse = browse(browseId)
    suspend fun getMoodPlaylists(): BrowseResponse = browse("FEmusic_moods_and_genres")
}
