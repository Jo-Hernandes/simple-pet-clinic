package com.jonathas.petclinic.ui.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.jonathas.httpservice.model.ApiResponseError
import com.jonathas.petclinic.R
import com.jonathas.petclinic.databinding.FragmentMainBinding
import com.jonathas.petclinic.ui.ui.main.domain.MainViewModelProvider
import com.jonathas.petclinic.utils.lazyViewLifecycleAwareProperty
import com.jonathas.petclinic.utils.showDialog
import com.jonathas.petclinic.utils.showSnackbar
import com.jonathas.petclinic.utils.viewLifecycleAwareProperty

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: MainViewModel by activityViewModels { MainViewModelProvider() }

    private var binding: FragmentMainBinding by viewLifecycleAwareProperty()

    private val petListAdapter: PetsAdapter by lazyViewLifecycleAwareProperty {
        PetsAdapter(viewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        FragmentMainBinding.inflate(inflater).apply {
            lifecycleOwner = viewLifecycleOwner
            binding = this
            binding.viewModel = this@MainFragment.viewModel
            rvPetsList.adapter = petListAdapter
        }.root

    override fun onStart() {
        super.onStart()
        viewModel.loadPetList()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
    }

    private fun observeData() = with(viewModel) {
        petList.observe(viewLifecycleOwner) {
            petListAdapter.submitList(it)
        }

        showErrorSnackbar.observe(viewLifecycleOwner) {
            showSnackbar(
                anchorView = binding.root,
                actionText = R.string.button_retry,
                textRes = when (it) {
                    ApiResponseError.Empty -> R.string.error_empty
                    ApiResponseError.InternalServerError -> R.string.error_server_error
                    ApiResponseError.NoConnection -> R.string.error_no_connection
                    ApiResponseError.NotFound -> R.string.error_not_found
                    ApiResponseError.Unknown -> R.string.error_unknown
                }
            ) { viewModel.loadPetList() }
        }

        showAlert.observe(viewLifecycleOwner) {
            showDialog(
                title = R.string.alert_title,
                buttonText = R.string.alert_close,
                message = it
            )
        }
    }
}