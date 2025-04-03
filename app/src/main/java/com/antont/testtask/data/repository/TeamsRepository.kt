package com.antont.testtask.data.repository

import android.content.Context
import com.antont.testtask.data.models.TeamsResponse
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class TeamsRepository(private val context: Context) {
    private val gson = Gson()

    fun getTeamsData(): Flow<TeamsResponse> = flow {
        val jsonString = context.assets.open("teams_data.json").bufferedReader().use { it.readText() }
        val teamsData = gson.fromJson(jsonString, TeamsResponse::class.java)
        emit(teamsData)
    }.flowOn(Dispatchers.IO)

    fun getCountries(): Flow<List<String>> = flow {
        val teamsData = getTeamsDataSync()
        emit(teamsData.teams.map { it.country }.distinct())
    }.flowOn(Dispatchers.IO)

    fun getLeaguesByCountry(country: String): Flow<List<String>> = flow {
        val teamsData = getTeamsDataSync()
        emit(teamsData.teams.filter { it.country == country }.map { it.league })
    }.flowOn(Dispatchers.IO)

    fun getTeamsByLeague(league: String): Flow<List<String>> = flow {
        val teamsData = getTeamsDataSync()
        emit(teamsData.teams.find { it.league == league }?.teams ?: emptyList())
    }.flowOn(Dispatchers.IO)

    private fun getTeamsDataSync(): TeamsResponse {
        val jsonString = context.assets.open("teams_data.json").bufferedReader().use { it.readText() }
        return gson.fromJson(jsonString, TeamsResponse::class.java)
    }
} 