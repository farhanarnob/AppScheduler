package com.farhanarnob.appscheduler.ui.createOrUpdate

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.farhanarnob.appscheduler.R
import com.farhanarnob.appscheduler.adapter.AppInfoArrayAdapter
import com.farhanarnob.appscheduler.base.BaseAppApplication
import com.farhanarnob.appscheduler.base.BaseFragment
import com.farhanarnob.appscheduler.databinding.FragmentCreateOrUpdateBinding
import com.farhanarnob.appscheduler.model.PackageInfo
import com.farhanarnob.appscheduler.util.DateUtility
import com.farhanarnob.appscheduler.util.UIUtility


class CreateOrUpdateScheduleFragment : BaseFragment() {

    private var timePicker: TimePickerDialog? = null
    private lateinit var viewModel: CreateOrUpdateScheduleViewModel
    private lateinit var binding: FragmentCreateOrUpdateBinding
    private var appResolveInfo: PackageInfo? = null
    private var scheduleTime: Long? = null
    private val args: CreateOrUpdateScheduleFragmentArgs by navArgs()
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
            initListener()
        }
    }

    private fun initListener() {
        binding.tvTime.setOnClickListener(timeDateOnClickListener)
        binding.fabSave.setOnClickListener(saveOnClickListener)
    }

    private fun onBackPressed() {
        findNavController().navigateUp()
    }
    private fun appSpinner(list: List<PackageInfo>) {
        val adapter = AppInfoArrayAdapter(
            requireContext(),
            R.layout.spinner_app, list
        )
        binding.spAppSelection.adapter = adapter
        val position = if(args.name != null && args.appName != null
            && args.pkgName != null && args.scheduleTime != 0L){
            scheduleTime = args.scheduleTime
            binding.tvTime.text = DateUtility.getTimeInString(
                DateUtility.WITH_SEC_DATE_FORMAT, scheduleTime!!
            )
            list.indexOf(PackageInfo(name = args.name!!,
                appName = args.appName!!, packageName = args.pkgName!!))
        }else 0

        binding.spAppSelection.setSelection(position)
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
            viewModel = ViewModelProvider(this@CreateOrUpdateScheduleFragment,
                factory)[CreateOrUpdateScheduleViewModel::class.java]
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
        viewModel.success.observe(viewLifecycleOwner){
            when(it){
                is Int ->{
                    UIUtility.generalDialogue(requireActivity(),
                        getString(R.string.warning),
                        getString(it))
                }
                is Boolean ->{
                    if(it){
                        onBackPressed()
                    }else{
                        UIUtility.generalDialogue(requireActivity(),
                            getString(R.string.warning),
                            getString(R.string.time_conflict))
                    }
                }
            }
        }
    }
    private val timeDateOnClickListener = View.OnClickListener {
        val time = System.currentTimeMillis()
        timePicker?.dismiss()
        timePicker = null
        timePicker = DateUtility.timePicker(
            getString(R.string.select_time),
            requireActivity(),
            time
        ) { view, hourOfDay, minute ->
            val scheduledTime = DateUtility.getTimeFromTimePicker(time,hourOfDay,minute)
            scheduleTime = if(time>scheduledTime){
                DateUtility.addADay(scheduledTime)
            }else{
                scheduledTime
            }
            binding.tvTime.text = DateUtility.getTimeInString(
                DateUtility.WITH_SEC_DATE_FORMAT, scheduleTime!!
            )
        }
    }

    private val saveOnClickListener = View.OnClickListener {
        viewModel.saveASchedule(requireContext(),appResolveInfo,scheduleTime, args)
    }
}