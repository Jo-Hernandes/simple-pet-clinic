package com.jonathas.petclinic.ui.ui.error

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jonathas.petclinic.R


class SettingsErrorFragment : BottomSheetDialogFragment() {
    private var param1: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ERROR_ARG)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings_error, container, false)
    }

    companion object {
        private const val ERROR_ARG = "ERROR_MESSAGE_ARGUMENT"

        @JvmStatic
        fun newInstance(@StringRes errorText: Int) =
            SettingsErrorFragment().apply {
                arguments = Bundle().apply {
                    putInt(ERROR_ARG, errorText)
                }
            }
    }


    override fun onCreateDialog(savedInstanceState: Bundle?) =
        (super.onCreateDialog(savedInstanceState)).apply {
            setOnShowListener {
                BottomSheetBehavior
                    .from(
                        findViewById<View>(
                            com.google.android.material.R.id.design_bottom_sheet
                        ) ?: return@setOnShowListener
                    )
                    .apply {
                        skipCollapsed = true
                        state = BottomSheetBehavior.STATE_EXPANDED
                    }
//                    .addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
//                        override fun onSlide(bottomSheet: View, slideOffset: Float) = Unit
//
//                        override fun onStateChanged(bottomSheet: View, newState: Int) {
//                            if (newState == BottomSheetBehavior.STATE_DRAGGING) behavior.state =
//                                BottomSheetBehavior.STATE_EXPANDED
//                        }
//                    })
            }
            setCanceledOnTouchOutside(false)
        }

}