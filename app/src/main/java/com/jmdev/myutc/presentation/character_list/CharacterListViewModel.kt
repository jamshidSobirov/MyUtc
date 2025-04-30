package com.jmdev.myutc.presentation.character_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmdev.myutc.data.model.Character
import com.jmdev.myutc.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CharacterListViewModel(
    private val repository: CharacterRepository
) : ViewModel() {

    private val _characters = MutableStateFlow<List<Character>>(emptyList())
    val characters: StateFlow<List<Character>> = _characters

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _hasError = MutableStateFlow(false)
    val hasError: StateFlow<Boolean> = _hasError

    private var currentPage = 1

    init {
        loadNextCharacters()
    }

    fun loadNextCharacters() {
        if (_isLoading.value) return

        viewModelScope.launch {
            _isLoading.value = true
            _hasError.value = false
            try {
                val newCharacters = repository.loadCharacters(currentPage)
                _characters.value += newCharacters
                currentPage++
            } catch (e: Exception) {
                _hasError.value = true
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun retryLoadingPage() {
        loadNextCharacters()
    }

    fun resetCharacters() {
        viewModelScope.launch {
            _isLoading.value = true
            _hasError.value = false
            _characters.value = emptyList()
            currentPage = 1
            try {
                val newCharacters = repository.loadCharacters(currentPage)
                _characters.value = newCharacters
                currentPage++
            } catch (e: Exception) {
                _hasError.value = true
            } finally {
                _isLoading.value = false
            }
        }
    }
}