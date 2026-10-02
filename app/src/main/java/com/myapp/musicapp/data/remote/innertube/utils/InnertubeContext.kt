package com.myapp.musicapp.data.remote.innertube.utils

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*

@Serializable
data class InnertubeContext(
    val client: Client
) {
    @Serializable
    data class Client(
        val clientName: String,
        val clientVersion: String,
        val platform: String = "DESKTOP",
        val hl: String = "en",
        val gl: String = "US",
        val visitorData: String? = null,
        val androidSdkVersion: Int? = null
    )

    fun toJsonObject(): JsonObject {
        return buildJsonObject {
            putJsonObject("client") {
                put("clientName", client.clientName)
                put("clientVersion", client.clientVersion)
                put("platform", client.platform)
                put("hl", client.hl)
                put("gl", client.gl)
                client.visitorData?.let { put("visitorData", it) }
                client.androidSdkVersion?.let { put("androidSdkVersion", it) }
            }
        }
    }
}
