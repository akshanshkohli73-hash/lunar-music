package com.myapp.musicapp.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myapp.musicapp.data.local.database.entities.SearchHistoryEntity
import com.myapp.musicapp.data.repository.SearchRepository
import com.myapp.musicapp.domain.model.SearchResult
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchRepository: SearchRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<SearchResult?>(null)
    val searchResults: StateFlow<SearchResult?> = _searchResults.asStateFlow()

    private val _suggestions = MutableStateFlow<List<String>>(emptyList())
    val suggestions: StateFlow<List<String>> = _suggestions.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    private val _selectedFilter = MutableStateFlow("All")
    val selectedFilter: StateFlow<String> = _selectedFilter.asStateFlow()

    val searchHistory: StateFlow<List<SearchHistoryEntity>> =
        searchRepository.getSearchHistory().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun onQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
        if (newQuery.isNotBlank()) {
            viewModelScope.launch {
                _suggestions.value = searchRepository.getSuggestions(newQuery)
            }
        } else {
            _suggestions.value = emptyList()
        }
    }

    fun search(query: String = searchQuery.value) {
        if (query.isBlank()) return
        viewModelScope.launch {
            _isSearching.value = true
            _searchResults.value = searchRepository.search(query, _selectedFilter.value)
            _isSearching.value = false
        }
    }

    fun setFilter(filter: String) {
        _selectedFilter.value = filter
        if (searchQuery.value.isNotBlank()) {
            search()
        }
    }

    fun removeHistoryItem(item: SearchHistoryEntity) {
        viewModelScope.launch {
            searchRepository.removeHistoryItem(item)
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            searchRepository.clearHistory()
        }
    }
}
