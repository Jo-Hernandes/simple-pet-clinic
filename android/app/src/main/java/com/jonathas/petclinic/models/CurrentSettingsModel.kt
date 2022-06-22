package com.jonathas.petclinic.models

data class CurrentSettingsModel(
    val showCallButton: Boolean = false,
    val showChatButton: Boolean = false,
    val bannerText: String? = null
)
