package com.jonathas.petclinic.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner

inline fun <reified T : ViewModel> ViewModelStoreOwner.viewModel(
    factory: ViewModelProvider.Factory
): Lazy<T> {
    return lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this, factory)[T::class.java]
    }
}
