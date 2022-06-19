package com.jonathas.petclinic

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.jonathas.petclinic.model.MainDestinations
import com.jonathas.petclinic.model.Routes
import com.jonathas.petclinic.ui.components.ClinicScaffold
import com.jonathas.petclinic.ui.components.ClinicSnackbar
import com.jonathas.petclinic.ui.components.MainScreen
import com.jonathas.petclinic.ui.theme.PetClinicTheme

@Composable
fun PetClinicApp() {
    PetClinicTheme {
        val appState = rememberPetAppState()

        ClinicScaffold(
            snackbarHost = {
                SnackbarHost(
                    hostState = it,
                    modifier = Modifier.systemBarsPadding(),
                    snackbar = { snackbarData -> ClinicSnackbar(snackbarData) }
                )
            },

            scaffoldState = appState.scaffoldState

        ) { innerPaddingModifier ->

            NavHost(
                navController = appState.navController,
                startDestination = MainDestinations.HOME_ROUTE,
                modifier = Modifier.padding(innerPaddingModifier)
            ) {
                navigation(
                    route = MainDestinations.HOME_ROUTE,
                    startDestination = Routes.MAIN.route
                ) {
                    composable(Routes.MAIN.route) {
                        MainScreen()
                    }
                }
            }
        }
    }
}