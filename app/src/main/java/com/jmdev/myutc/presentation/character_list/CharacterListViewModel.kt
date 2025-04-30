package com.jmdev.myutc.presentation.character_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmdev.myutc.data.model.Character
import com.jmdev.myutc.data.repository.PageState
import com.jmdev.myutc.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CharacterListViewModel(
    private val repository: CharacterRepository
) : ViewModel() {

    private val _pages = MutableStateFlow<MutableList<PageState<List<Character>>>>(mutableListOf())
    val pages: StateFlow<List<PageState<List<Character>>>> = _pages

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    private var currentPage = 1

    init {
        loadNextPage()
    }

    fun loadNextPage() {
        _pages.value = _pages.value.toMutableList().apply {
            add(PageState.Loading)
        }

        viewModelScope.launch {
            val result = repository.loadCharacters(currentPage)
            _pages.value = _pages.value.toMutableList().apply {
                removeAt(size - 1)
                add(result)
            }
            if (result is PageState.Success) {
                currentPage++
            }
        }
    }

    fun resetPagination() {
        viewModelScope.launch {
            _isRefreshing.value = true
            currentPage = 1
            _pages.value = mutableListOf()
            val result = repository.loadCharacters(currentPage)
            _pages.value = _pages.value.toMutableList().apply {
                add(result) // Add the first page
            }
            if (result is PageState.Success) {
                currentPage = 2
            }
            _isRefreshing.value = false
        }
    }

    fun retryPage(pageIndex: Int) {
        _pages.value = _pages.value.toMutableList().apply {
            set(pageIndex, PageState.Loading)
        }

        viewModelScope.launch {
            val result = repository.loadCharacters(pageIndex + 1)
            _pages.value = _pages.value.toMutableList().apply {
                set(pageIndex, result)
            }
            if (result is PageState.Success) {
                currentPage = pageIndex + 2
            }
        }
    }
}