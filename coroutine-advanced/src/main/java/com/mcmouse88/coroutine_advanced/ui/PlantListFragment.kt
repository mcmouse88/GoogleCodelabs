package com.mcmouse88.coroutine_advanced.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.mcmouse88.coroutine_advanced.PlantListViewModel
import com.mcmouse88.coroutine_advanced.PlantRepository
import com.mcmouse88.coroutine_advanced.R
import com.mcmouse88.coroutine_advanced.databinding.FragmentPlantsListBinding
import com.mcmouse88.coroutine_advanced.utils.Injector
import com.mcmouse88.sunflower.ui.PlantAdapter

class PlantListFragment : Fragment() {

    private val viewModel: PlantListViewModel by viewModels {
        Injector.providePlantListViewModel(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentPlantsListBinding.inflate(inflater, container, false)
        context ?: return binding.root

        provideOptionMenu()

        // show the spinner when [spinner] is true
        viewModel.spinner.observe(viewLifecycleOwner) { show ->
            binding.spinner.isVisible = show
        }

        // Show a snackbar whenever the [snackbar] is updated a non-null value
        viewModel.snackbar.observe(viewLifecycleOwner) { text ->
            text?.let {
                Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()
                viewModel.onSnackbarShown()
            }
        }

        val adapter = PlantAdapter()
        binding.rvPlantList.adapter = adapter
        subscribeUi(adapter)

        return binding.root
    }

    private fun provideOptionMenu() {
        requireActivity().addMenuProvider(object : MenuProvider {

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_plant_list, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.filter_zone -> {
                        updateData()
                        true
                    }

                    else -> false
                }
            }
        })
    }

    private fun subscribeUi(adapter: PlantAdapter) {
        viewModel.plantsUsingFlow.observe(viewLifecycleOwner) { plants ->
            adapter.submitList(plants)
        }
    }

    private fun updateData() {
        with(viewModel) {
            if (isFiltered()) {
                clearGrowZoneNumber()
            } else {
                setGrowZoneNumber(9)
            }
        }
    }
}

/**
 * Factory for creating a [PlantListViewModel] with a constructor that takes a [PlantRepository].
 */
class PlantListViewModelFactory(
    private val repository: PlantRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        PlantListViewModel(repository) as T
}