package com.jonathas.petclinic.utils

import android.view.View
import androidx.annotation.MainThread
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.jonathas.petclinic.R
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


fun <T : Any> Fragment.viewLifecycleAwareProperty() = ViewLifecycleAwareReadWriteProperty<T>(this)

class ViewLifecycleAwareReadWriteProperty<T : Any>(
    private val fragment: Fragment
) : ReadWriteProperty<Fragment, T> {

    private var propertyValue: T? = null

    init {
        val propertyLifecycleObserve = object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                propertyValue = null
            }
        }
        val initializerLifecycleObserver = object : DefaultLifecycleObserver {
            override fun onCreate(owner: LifecycleOwner) {
                fragment.viewLifecycleOwnerLiveData.observe(fragment) { lifecycleOwner ->
                    lifecycleOwner?.lifecycle?.addObserver(propertyLifecycleObserve)
                }
            }
        }
        fragment.lifecycle.addObserver(initializerLifecycleObserver)
    }

    @MainThread
    override fun setValue(thisRef: Fragment, property: KProperty<*>, value: T) {
        propertyValue = value
    }

    @MainThread
    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        return propertyValue
            ?: throw IllegalStateException("This property should not be read when the lifecycle owner is not active")
    }
}

fun <T : Any> Fragment.lazyViewLifecycleAwareProperty(
    initializer: (() -> T)
) = LazyViewLifecycleAwareProperty(this, initializer)

class LazyViewLifecycleAwareProperty<T : Any>(
    private val fragment: Fragment,
    private val initializer: (() -> T)
) : ReadOnlyProperty<Fragment, T> {

    private var propertyValue: T? = null

    init {
        val propertyLifecycleObserve = object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                propertyValue = null
            }
        }
        val initializerLifecycleObserver = object : DefaultLifecycleObserver {
            override fun onCreate(owner: LifecycleOwner) {
                fragment.viewLifecycleOwnerLiveData.observe(fragment) { lifecycleOwner ->
                    lifecycleOwner?.lifecycle?.addObserver(propertyLifecycleObserve)
                }
            }
        }
        fragment.lifecycle.addObserver(initializerLifecycleObserver)
    }

    @MainThread
    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        return propertyValue ?: initializer.invoke().also { propertyValue = it }
    }
}


fun showSnackbar(
    anchorView: View,
    @StringRes textRes: Int,
    @StringRes actionText: Int? = null,
    action: (() -> Unit)? = null
) =
    Snackbar
        .make(
            anchorView,
            textRes,
            Snackbar.LENGTH_LONG
        )
        .apply {
            if (actionText != null && action != null) {
                setAction(actionText) { action() }
                setActionTextColor(context.getColor(R.color.powder))
            }
            view.setBackgroundColor(context.getColor(R.color.wild_blue))
            show()
        }

fun Fragment.showDialog(
    @StringRes title: Int,
    @StringRes message: Int,
    @StringRes buttonText : Int
) = context?.let {
    MaterialAlertDialogBuilder(it, com.google.android.material.R.style.MaterialAlertDialog_Material3).apply {
        setTitle(title)
        setMessage(message)
        setPositiveButton(buttonText, null)
        create()
        show()
    }
}