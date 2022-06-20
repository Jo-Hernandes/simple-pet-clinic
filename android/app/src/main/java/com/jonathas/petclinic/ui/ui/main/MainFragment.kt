package com.jonathas.petclinic.ui.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.jonathas.petclinic.R
import com.jonathas.petclinic.databinding.FragmentMainBinding
import com.jonathas.petclinic.ui.ui.main.domain.MainViewModelProvider
import com.jonathas.petclinic.utils.lazyViewLifecycleAwareProperty
import com.jonathas.petclinic.utils.showDialog
import com.jonathas.petclinic.utils.showSnackbar
import com.jonathas.petclinic.utils.viewLifecycleAwareProperty

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels { MainViewModelProvider() }

    private var binding: FragmentMainBinding by viewLifecycleAwareProperty()

    private val petListAdapter: PetsAdapter by lazyViewLifecycleAwareProperty {
        PetsAdapter(viewModel)
    }

    private val navController: NavController get() = findNavController()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        FragmentMainBinding.inflate(
            inflater,
            container,
            false
        ).apply {
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

        screenEvent.observe(viewLifecycleOwner) { event ->
            when (event) {
                is MainScreenEvent.ShowAlert -> showAlert(event.message)
                is MainScreenEvent.ShowErrorScreen -> showErrorScreen(event.message)
                is MainScreenEvent.ShowSnackBar -> showRetrySnackbar(event.message)
                is MainScreenEvent.ShowWebSContent -> showWebContent(event.contentUrl)
                else -> Unit
            }
        }
    }

    private fun showAlert(@StringRes message: Int) = showDialog(
        title = R.string.alert_title,
        buttonText = R.string.alert_close,
        message = message
    )

    private fun showErrorScreen(@StringRes message: Int) =
        navController.navigate(MainFragmentDirections.actionShowError(getString(message)))

    private fun showRetrySnackbar(@StringRes message: Int) = showSnackbar(
        anchorView = binding.root,
        actionText = R.string.button_retry,
        textRes = message
    ) { viewModel.loadPetList() }

    private fun showWebContent(contentUrl: String) =
        navController.navigate(MainFragmentDirections.actionShowWebview(contentUrl))

}