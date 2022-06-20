package com.jonathas.petclinic.ui.ui.main

import androidx.annotation.StringRes

sealed class MainScreenEvent {

    class ShowSnackBar(@StringRes val message : Int) : MainScreenEvent()

    class ShowErrorScreen(@StringRes val message: Int) : MainScreenEvent()

    class ShowAlert(@StringRes val message: Int) : MainScreenEvent()

    class ShowWebSContent(val contentUrl: String) : MainScreenEvent()

    object DismissError : MainScreenEvent()
}
