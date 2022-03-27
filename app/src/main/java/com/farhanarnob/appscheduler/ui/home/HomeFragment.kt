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


class HomeFragment : BaseFragment() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding
    private lateinit var database: AppDatabase
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
                    val mainIntent = Intent(Intent.ACTION_MAIN, null)
                    mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
                    val pkgAppsList: List<ResolveInfo> =
                        requireActivity().packageManager.queryIntentActivities(mainIntent, 0)
                    android.app.AlertDialog
                        .Builder(requireContext())
                        .setMessage(pkgAppsList.map {
                            it.loadLabel(requireActivity().packageManager).toString()+
                                    "***"+ it.activityInfo.packageName.toString()+
                                    "###"+ it.activityInfo.name.toString()
                        }.toString())
                        .setCancelable(false)
                        .setPositiveButton("Yes") { _, _ ->
                            val activity: ActivityInfo = pkgAppsList[pkgAppsList.indices.random()].activityInfo
                            val intent = Intent(Intent.ACTION_MAIN)
                            intent.addCategory(Intent.CATEGORY_LAUNCHER)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                                    Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
                            intent.component = ComponentName(
                                activity.applicationInfo.packageName,
                                activity.name
                            )
                            startActivity(intent)
                        }
                        .setNegativeButton("No") { _, _ -> }
                        .create().show()
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
            toolbarSetup(this@HomeFragment::onBackPressed,R.string.schedule_list)
            hideToolbarBackIcon()
            UIUtility.showFullScreen(requireActivity())
            adapterInitialize()
            observe()
            viewModel.startToCheckApp(requireContext())
        }
    }

    private fun onBackPressed() {

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