package com.jonathas.petclinic.model

object MainDestinations {
    const val HOME_ROUTE = "home"
}

enum class Routes(
    val route: String
) {
    MAIN("home/main"),
    LINK("home/link"),
}