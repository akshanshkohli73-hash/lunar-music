package com.myapp.musicapp.player

import com.myapp.musicapp.domain.model.Song
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class QueueManager {
    private val _queue = MutableStateFlow<List<Song>>(emptyList())
    val queue: StateFlow<List<Song>> = _queue.asStateFlow()

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex.asStateFlow()

    fun setQueue(songs: List<Song>, startIndex: Int = 0) {
        _queue.value = songs
        _currentIndex.value = startIndex.coerceIn(0, (songs.size - 1).coerceAtLeast(0))
    }

    fun getCurrentSong(): Song? {
        val q = _queue.value
        val idx = _currentIndex.value
        return if (q.isNotEmpty() && idx in q.indices) q[idx] else null
    }

    fun next(): Song? {
        val q = _queue.value
        if (q.isEmpty()) return null
        val nextIdx = (_currentIndex.value + 1) % q.size
        _currentIndex.value = nextIdx
        return q[nextIdx]
    }

    fun previous(): Song? {
        val q = _queue.value
        if (q.isEmpty()) return null
        val prevIdx = if (_currentIndex.value - 1 < 0) q.size - 1 else _currentIndex.value - 1
        _currentIndex.value = prevIdx
        return q[prevIdx]
    }
}
