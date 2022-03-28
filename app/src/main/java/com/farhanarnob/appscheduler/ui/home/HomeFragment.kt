package com.farhanarnob.appscheduler.ui.home

import android.content.ComponentName
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.farhanarnob.appscheduler.R
import com.farhanarnob.appscheduler.adapter.ScheduleAdapter
import com.farhanarnob.appscheduler.base.BaseAppApplication
import com.farhanarnob.appscheduler.base.BaseFragment
import com.farhanarnob.appscheduler.databinding.FragmentHomeBinding
import com.farhanarnob.appscheduler.model.Schedule
import com.farhanarnob.appscheduler.util.UIUtility
import com.hellodoc24.hellodoc24patientapp.data.source.database.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class HomeFragment : BaseFragment() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding
    private var scheduleListListAdapter: ScheduleAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_home, container, false
        )
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.clear_all_schedule -> {
                lifecycleScope.launchWhenResumed {
                    UIUtility.yesNoDialogue(activity = requireActivity(),
                        title = getString(R.string.warning),
                        message = getString(R.string.want_to_delete_all),
                        cancelable = false,
                        positiveButtonMessage = R.string.yes, negativeButtonMessage = R.string.no,
                        negativeListener = null
                    ) { _, _ ->
                        CoroutineScope(Dispatchers.IO).launch {
                            (context?.applicationContext as BaseAppApplication)
                                .database.scheduleDao().deleteAllSchedules()
                        }
                    }
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenResumed {
            viewModelSetup()
            navigateSetup(this@HomeFragment::navigateTo)
            toolbarSetup({ } ,R.string.schedule_list)
            hideToolbarBackIcon()
            UIUtility.showFullScreen(requireActivity())
            adapterInitialize()
            observe()
        }
    }

    private fun viewModelSetup() {
        with(requireContext().applicationContext as BaseAppApplication) {
            val factory = HomeViewModelFactory(
                database,scheduleRepository
            )
            viewModel = ViewModelProvider(this@HomeFragment, factory)[HomeViewModel::class.java]
        }
    }

    private fun adapterInitialize() {
        scheduleListListAdapter = ScheduleAdapter(object :ScheduleAdapter.ScheduleAdapterListener{
            override fun updateItemClick(schedule: Schedule) {
                lifecycleScope.launchWhenResumed {
                    UIUtility.addButtonVisibility(requireActivity(),View.GONE)
                    findNavController().navigate(
                        HomeFragmentDirections.actionHomeFragmentToCreateOrUpdateScheduleFragment(
                            appName = schedule.appName,
                            scheduleTime = schedule.scheduledTime,
                            name = schedule.name,
                            pkgName = schedule.packageName
                        ))
                }
            }

            override fun deleteItemClick(schedule: Schedule) {
                viewModel.deleteSchedule(schedule)
            }

        })
        binding.rvHolder.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL, false
        )
        binding.rvHolder.adapter = scheduleListListAdapter
    }

    private fun observe() {
        viewModel.appList.observe(viewLifecycleOwner){
            lifecycleScope.launchWhenResumed {
                UIUtility.exitFullScreen(requireActivity())
                scheduleListListAdapter?.submitList(it)
                val visibility = if(it.isNullOrEmpty()) View.VISIBLE else View.GONE
                binding.emptyParentLayout.emptyParentLayout.visibility = visibility

            }
        }
    }
    private fun navigateTo(id:Int){
        findNavController().navigate(id)
    }


}