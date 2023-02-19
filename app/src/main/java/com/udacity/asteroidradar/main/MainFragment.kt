package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.database.AsteroidDataBase

import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.model.Repo

class MainFragment : Fragment() {

    private lateinit var adapter: MainAdapter

    private val dataBase by lazy {AsteroidDataBase.getDataBase(requireContext())}
    private val Repository by lazy {Repo(dataBase)}
    private val mainViewModel: MainViewModel by viewModels {MainViewModelFactory(Repository)}
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentMainBinding.inflate(inflater)


         adapter = MainAdapter(MainAdapter.OnClickListener {
                    mainViewModel.onAstroidClick(it)
        })

        //observe navigation
        mainViewModel.objectnavigation.observe(viewLifecycleOwner, Observer {
            if(it != null)
            {
                this.findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                mainViewModel.onAstroidClickDone()

            }
        })


        binding.viewModel = mainViewModel
        binding.asteroidRecycler.adapter = adapter

        //set data to rec
        mainViewModel.asteroidList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })



        binding.lifecycleOwner = this
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.show_saved_menu -> mainViewModel.onListItemSelected(MenuSelectionEnum.SAVE)
            R.id.show_week_menu -> mainViewModel.onListItemSelected(MenuSelectionEnum.WEEK)
            R.id.show_today_menu -> mainViewModel.onListItemSelected(MenuSelectionEnum.TODAY)
        }
        return true
    }

    enum class MenuSelectionEnum{
        SAVE,
        TODAY,
        WEEK
    }

}
