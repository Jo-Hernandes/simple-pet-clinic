package com.jonathas.petclinic.ui.ui.main

import androidx.annotation.StringRes
import com.jonathas.httpservice.model.ApiResponseError

sealed class MainScreenEvent {

    class ShowSnackBar(@StringRes val message: Int) : MainScreenEvent()

    class ShowErrorScreen(val apiError: ApiResponseError) : MainScreenEvent()

    class ShowCommunicationAlert(val isWorkHour: Boolean) : MainScreenEvent()

    class ShowWebContent(val contentUrl: String) : MainScreenEvent()

    object DismissError : MainScreenEvent()
}
