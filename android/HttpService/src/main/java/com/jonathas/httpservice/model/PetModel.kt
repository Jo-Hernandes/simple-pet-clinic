package com.jonathas.httpservice.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PetModel(
    @SerialName("content_url") val contentUrl: String,
    @SerialName("date_added") val dateAdded: String,
    @SerialName("image_url") val imageUrl: String,
    val title: String
)