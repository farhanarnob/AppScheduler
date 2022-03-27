package com.farhanarnob.appscheduler.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.farhanarnob.appscheduler.R
import com.farhanarnob.appscheduler.base.BaseAppApplication
import com.farhanarnob.appscheduler.base.BaseFragment
import com.farhanarnob.appscheduler.databinding.FragmentHomeBinding
import com.farhanarnob.appscheduler.util.UIUtility
import com.hellodoc24.hellodoc24patientapp.data.source.database.AppDatabase
class HomeFragment : BaseFragment() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding
    private lateinit var database: AppDatabase
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_home, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenResumed {
            viewModelSetup()
            toolbarSetup(this@HomeFragment::onBackpressed,R.string.schedule_list)
            UIUtility.exitFullScreen(requireActivity())
            observe()

        }
    }

    private fun onBackpressed() {
        TODO("Not yet implemented")
    }

    private fun viewModelSetup() {
        with(requireContext().applicationContext as BaseAppApplication) {
            val factory = HomeViewModelFactory(
                database
            )
            viewModel = ViewModelProvider(this@HomeFragment, factory)[HomeViewModel::class.java]
        }
    }

    private fun observe() {

    }

}