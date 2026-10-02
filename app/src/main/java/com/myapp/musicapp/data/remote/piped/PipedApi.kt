package com.myapp.musicapp.data.remote.piped

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class PipedApi(private val client: HttpClient) {

    companion object {
        private const val BASE_URL = "https://pipedapi.kavin.rocks"
    }

    suspend fun getStreamInfo(videoId: String): PipedStreamInfo {
        return client.get("$BASE_URL/streams/$videoId").body()
    }

    suspend fun getAudioUrl(videoId: String): String {
        val info = getStreamInfo(videoId)
        return info.audioStreams
            .maxByOrNull { it.bitrate }
            ?.url
            ?: throw Exception("No audio stream found on Piped")
    }
}
