package com.farhanarnob.appscheduler.ui.createOrUpdate

import android.content.ComponentName
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.farhanarnob.appscheduler.R
import com.farhanarnob.appscheduler.adapter.AppInfoArrayAdapter
import com.farhanarnob.appscheduler.adapter.ScheduleAdapter
import com.farhanarnob.appscheduler.base.BaseAppApplication
import com.farhanarnob.appscheduler.base.BaseFragment
import com.farhanarnob.appscheduler.databinding.FragmentCreateOrUpdateBinding
import com.farhanarnob.appscheduler.databinding.FragmentHomeBinding
import com.farhanarnob.appscheduler.model.Schedule
import com.farhanarnob.appscheduler.util.UIUtility
import com.hellodoc24.hellodoc24patientapp.data.source.database.AppDatabase


class CreateOrUpdateScheduleFragment : BaseFragment() {

    private lateinit var viewModel: CreateOrUpdateScheduleViewModel
    private lateinit var binding: FragmentCreateOrUpdateBinding
    private var appResolveInfo: ResolveInfo? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_create_or_update, container, false
        )
        setHasOptionsMenu(false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenResumed {
            viewModelSetup()
            toolbarSetup(this@CreateOrUpdateScheduleFragment::onBackPressed,R.string.add_a_schedule)
            showToolbarBackIcon()
            observe()
            viewModel.loadInstalledAppList(requireContext())
        }
    }

    private fun onBackPressed() {
        findNavController().navigateUp()
    }
    private fun appSpinner(list: List<ResolveInfo>) {
        val adapter = AppInfoArrayAdapter(
            requireContext(),
            R.layout.spinner_app, list
        )
        binding.spAppSelection.adapter = adapter
        binding.spAppSelection.setSelection(0)
        binding.spAppSelection.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    appResolveInfo = list[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
    }
    private fun viewModelSetup() {
        with(requireContext().applicationContext as BaseAppApplication) {
            val factory = CreateOrUpdateScheduleModelFactory(
                database,scheduleRepository
            )
            viewModel = ViewModelProvider(this@CreateOrUpdateScheduleFragment, factory)[CreateOrUpdateScheduleViewModel::class.java]
        }
    }


    private fun observe() {
        viewModel.appList.observe(viewLifecycleOwner){
            lifecycleScope.launchWhenResumed {
                it?.let {
                    appSpinner(it)
                }
            }
        }
    }

}