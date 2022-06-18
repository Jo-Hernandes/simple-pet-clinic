package com.jonathas.httpservice.model

data class SettingsModel(
    val isCallEnabled: Boolean,
    val isChatEnabled: Boolean,
    val workHours: String
)