package com.myapp.musicapp.player.notification

import android.content.Context
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService

class MusicNotificationManager(
    private val context: Context,
    private val mediaSession: MediaSession
) {
    // MediaSessionService in Media3 manages system notifications automatically via session tokens
}
