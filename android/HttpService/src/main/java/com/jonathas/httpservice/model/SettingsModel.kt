package com.jonathas.httpservice.model

import kotlinx.serialization.Serializable

@Serializable
class SettingsModel {
    val isCallEnabled: Boolean? = null
    val isChatEnabled: Boolean? = null
    val workHours: String = ""
}