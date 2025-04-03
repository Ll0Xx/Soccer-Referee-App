package com.antont.testtask.data.models

data class LeagueData(
    val league: String,
    val country: String,
    val teams: List<String>
)

data class TeamsResponse(
    val teams: List<LeagueData>
) 