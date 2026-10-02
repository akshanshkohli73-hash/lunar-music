package com.myapp.musicapp.player

enum class RepeatMode {
    OFF,
    ONE,
    ALL;

    companion object {
        fun fromExoPlayer(repeatMode: Int): RepeatMode {
            return when (repeatMode) {
                androidx.media3.common.Player.REPEAT_MODE_ONE -> ONE
                androidx.media3.common.Player.REPEAT_MODE_ALL -> ALL
                else -> OFF
            }
        }
    }
}

sealed class PlayerEvent {
    data class PlaySong(val songId: String) : PlayerEvent()
    object TogglePlayPause : PlayerEvent()
    object SkipNext : PlayerEvent()
    object SkipPrevious : PlayerEvent()
    data class SeekTo(val positionMs: Long) : PlayerEvent()
    object ToggleShuffle : PlayerEvent()
    object CycleRepeatMode : PlayerEvent()
}
