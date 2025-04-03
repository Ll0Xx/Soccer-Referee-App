package com.antont.testtask.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.antont.testtask.data.models.TeamsResponse
import com.antont.testtask.data.repository.TeamsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class TeamsViewModel(private val repository: TeamsRepository) : ViewModel() {
    private val _countries = MutableStateFlow<List<String>>(emptyList())
    val countries: StateFlow<List<String>> = _countries

    private val _leagues = MutableStateFlow<List<String>>(emptyList())
    val leagues: StateFlow<List<String>> = _leagues

    private val _teams = MutableStateFlow<List<String>>(emptyList())
    val teams: StateFlow<List<String>> = _teams

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        loadCountries()
    }

    fun loadCountries() {
        viewModelScope.launch {
            repository.getCountries()
                .catch { e ->
                    _error.value = e.message
                }
                .collect { countries ->
                    _countries.value = countries
                }
        }
    }

    fun loadLeaguesByCountry(country: String) {
        viewModelScope.launch {
            repository.getLeaguesByCountry(country)
                .catch { e ->
                    _error.value = e.message
                }
                .collect { leagues ->
                    _leagues.value = leagues
                }
        }
    }

    fun loadTeamsByLeague(league: String) {
        viewModelScope.launch {
            repository.getTeamsByLeague(league)
                .catch { e ->
                    _error.value = e.message
                }
                .collect { teams ->
                    _teams.value = teams
                }
        }
    }

    class Factory(private val repository: TeamsRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TeamsViewModel::class.java)) {
                return TeamsViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
} 