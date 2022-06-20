package com.jonathas.petclinic.ui.ui.error

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.jonathas.petclinic.databinding.FragmentSettingsErrorBinding
import com.jonathas.petclinic.ui.ui.main.MainScreenEvent
import com.jonathas.petclinic.ui.ui.main.MainViewModel
import com.jonathas.petclinic.ui.ui.main.domain.MainViewModelProvider

class SettingsErrorFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels { MainViewModelProvider() }

    private val fragmentArgs: SettingsErrorFragmentArgs by navArgs()

    private val navController: NavController get() = findNavController()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentSettingsErrorBinding.inflate(
        inflater,
        container,
        false
    ).apply {
        lifecycleOwner = viewLifecycleOwner
        handler = viewModel
        errorText = fragmentArgs.message

    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeData()
    }

    private fun observeData() = with(viewModel) {
        screenEvent.observe(viewLifecycleOwner) {
            if (it is MainScreenEvent.DismissError) {
                navController.navigateUp()
            }
        }
    }

}