package com.myapp.musicapp.player

import android.content.ComponentName
import android.content.Context
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import com.myapp.musicapp.data.repository.MusicRepository
import com.myapp.musicapp.domain.model.Song
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PlayerConnection(
    private val context: Context,
    private val repository: MusicRepository
) {
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    private val _currentSong = MutableStateFlow<Song?>(null)
    val currentSong: StateFlow<Song?> = _currentSong.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _progress = MutableStateFlow(0L)
    val progress: StateFlow<Long> = _progress.asStateFlow()

    private val _duration = MutableStateFlow(0L)
    val duration: StateFlow<Long> = _duration.asStateFlow()

    private val _repeatMode = MutableStateFlow(RepeatMode.OFF)
    val repeatMode: StateFlow<RepeatMode> = _repeatMode.asStateFlow()

    private val _shuffleEnabled = MutableStateFlow(false)
    val shuffleEnabled: StateFlow<Boolean> = _shuffleEnabled.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

    private val _queue = MutableStateFlow<List<Song>>(emptyList())
    val queue: StateFlow<List<Song>> = _queue.asStateFlow()

    private var mediaController: MediaController? = null

    init {
        connect()
    }

    private fun connect() {
        val sessionToken = SessionToken(context, ComponentName(context, MusicService::class.java))
        val controllerFuture: ListenableFuture<MediaController> =
            MediaController.Builder(context, sessionToken).buildAsync()

        controllerFuture.addListener({
            try {
                mediaController = controllerFuture.get().also { controller ->
                    controller.addListener(object : Player.Listener {
                        override fun onIsPlayingChanged(isPlaying: Boolean) {
                            _isPlaying.value = isPlaying
                        }

                        override fun onRepeatModeChanged(repeatMode: Int) {
                            _repeatMode.value = RepeatMode.fromExoPlayer(repeatMode)
                        }

                        override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
                            _shuffleEnabled.value = shuffleModeEnabled
                        }
                    })
                    startProgressPolling(controller)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, MoreExecutors.directExecutor())
    }

    fun playSong(song: Song, queue: List<Song>? = null) {
        _currentSong.value = song
        if (queue != null) {
            _queue.value = queue
        }

        scope.launch {
            _isFavorite.value = repository.isFavorite(song.id)
            try {
                val streamUrl = repository.getStreamUrl(song.id)
                val mediaItem = MediaItem.Builder()
                    .setMediaId(song.id)
                    .setUri(streamUrl)
                    .setMediaMetadata(
                        MediaMetadata.Builder()
                            .setTitle(song.title)
                            .setArtist(song.artist)
                            .setAlbumTitle(song.album)
                            .setArtworkUri(song.thumbnailUrl?.let { Uri.parse(it) })
                            .build()
                    )
                    .build()

                mediaController?.apply {
                    setMediaItem(mediaItem)
                    prepare()
                    play()
                }

                repository.addToRecentlyPlayed(song)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun togglePlayPause() {
        mediaController?.let {
            if (it.isPlaying) it.pause() else it.play()
        }
    }

    fun skipNext() {
        val q = _queue.value
        val cur = _currentSong.value
        if (q.isNotEmpty() && cur != null) {
            val idx = q.indexOfFirst { it.id == cur.id }
            if (idx != -1 && idx + 1 < q.size) {
                playSong(q[idx + 1])
            }
        }
    }

    fun skipPrevious() {
        val q = _queue.value
        val cur = _currentSong.value
        if (q.isNotEmpty() && cur != null) {
            val idx = q.indexOfFirst { it.id == cur.id }
            if (idx > 0) {
                playSong(q[idx - 1])
            }
        }
    }

    fun seekTo(position: Long) {
        mediaController?.seekTo(position)
    }

    fun toggleShuffle() {
        mediaController?.let {
            it.shuffleModeEnabled = !it.shuffleModeEnabled
        }
    }

    fun cycleRepeatMode() {
        mediaController?.let {
            it.repeatMode = when (it.repeatMode) {
                Player.REPEAT_MODE_OFF -> Player.REPEAT_MODE_ALL
                Player.REPEAT_MODE_ALL -> Player.REPEAT_MODE_ONE
                else -> Player.REPEAT_MODE_OFF
            }
        }
    }

    fun toggleFavorite() {
        scope.launch {
            _currentSong.value?.let { song ->
                repository.toggleFavorite(song.id)
                _isFavorite.value = !_isFavorite.value
            }
        }
    }

    private fun startProgressPolling(controller: MediaController) {
        scope.launch {
            while (isActive) {
                if (controller.isPlaying) {
                    _progress.value = controller.currentPosition
                    _duration.value = controller.duration.coerceAtLeast(0)
                }
                delay(200)
            }
        }
    }
}
