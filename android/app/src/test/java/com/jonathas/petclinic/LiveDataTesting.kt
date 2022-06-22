package com.jonathas.petclinic

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

/**
 * Observes a [LiveData] until the `block` is done executing.
 */
fun <T> LiveData<T>.observeForTesting(block: (T) -> Unit) {
    val observer = Observer<T> { block(it) }
    try {
        observeForever(observer)
    } finally {
        removeObserver(observer)
    }
}
