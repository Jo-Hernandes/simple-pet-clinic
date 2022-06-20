package com.jonathas.petclinic.ui.ui.error

import androidx.lifecycle.LiveData

interface SettingsErrorHandler {

    val showLoading : LiveData<Boolean>

    fun onRetryPressed()

}