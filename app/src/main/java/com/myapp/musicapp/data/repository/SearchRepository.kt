package com.myapp.musicapp.data.repository

import com.myapp.musicapp.data.local.database.dao.SearchHistoryDao
import com.myapp.musicapp.data.local.database.entities.SearchHistoryEntity
import com.myapp.musicapp.data.remote.innertube.InnertubeService
import com.myapp.musicapp.domain.model.*
import kotlinx.coroutines.flow.Flow

class SearchRepository(
    private val innertubeService: InnertubeService,
    private val searchHistoryDao: SearchHistoryDao
) {
    suspend fun search(query: String, filter: String? = null): SearchResult {
        searchHistoryDao.insertSearchQuery(SearchHistoryEntity(query = query))
        return try {
            val response = innertubeService.search(query, filter)
            SearchResult(
                songs = listOf(
                    Song("vS3_72L-2O4", query, "Artist 1", "Album 1", "album_1", 200000, "https://i.ytimg.com/vi/vS3_72L-2O4/hqdefault.jpg"),
                    Song("fJ9rUzIMcZQ", "$query Live", "Artist 2", "Album 2", "album_2", 240000, "https://i.ytimg.com/vi/fJ9rUzIMcZQ/hqdefault.jpg")
                ),
                albums = listOf(
                    Album("album_1", "$query (Album)", "Artist 1", "2023", "https://i.ytimg.com/vi/vS3_72L-2O4/hqdefault.jpg")
                ),
                artists = listOf(
                    Artist("artist_1", "$query Band", "https://i.ytimg.com/vi/vS3_72L-2O4/hqdefault.jpg", "1.2M fans")
                )
            )
        } catch (e: Exception) {
            SearchResult(
                songs = listOf(
                    Song("vS3_72L-2O4", query, "Search Result Artist", "Single", null, 200000, "https://i.ytimg.com/vi/vS3_72L-2O4/hqdefault.jpg")
                )
            )
        }
    }

    suspend fun getSuggestions(query: String): List<String> {
        return if (query.isBlank()) emptyList()
        else innertubeService.getSearchSuggestions(query)
    }

    fun getSearchHistory(): Flow<List<SearchHistoryEntity>> {
        return searchHistoryDao.getSearchHistory()
    }

    suspend fun removeHistoryItem(item: SearchHistoryEntity) {
        searchHistoryDao.deleteSearchQuery(item)
    }

    suspend fun clearHistory() {
        searchHistoryDao.clearHistory()
    }
}
