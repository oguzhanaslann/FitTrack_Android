package com.oguzhanaslann.feature_reports.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.oguzhanaslann.common.onError
import com.oguzhanaslann.common.onSuccess
import com.oguzhanaslann.commonui.verticalLinearLayoutManaged
import com.oguzhanaslann.commonui.viewBinding
import com.oguzhanaslann.feature_reports.R
import com.oguzhanaslann.feature_reports.databinding.FragmentReportsBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class ReportsFragment : Fragment(R.layout.fragment_reports) {

    private val binding by viewBinding(FragmentReportsBinding::bind)

    private val reportsViewModel: ReportsViewModel by viewModels()

    private val reportsAdapter by lazy {
        ReportExerciseAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = reportsViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        initViews()
        subscribeObservers()
    }

    private fun initViews() = binding.run {
        dateNavigationContainer.setOnClickListener {
            showDateSelectionFragment()
        }

        moveForwardIcon.setOnClickListener {
            reportsViewModel.moveNextDayIfPossible()
        }

        moveBackwardsIcon.setOnClickListener {
            reportsViewModel.movePreviousDay()
        }

        rvReports.apply {
            verticalLinearLayoutManaged()
            adapter = reportsAdapter
        }
    }

    private fun showDateSelectionFragment() {
        val startDate = reportsViewModel.getSelectedDateOrNow()
        val now = Date()
        val constraintsBuilder = CalendarConstraints.Builder()
            .setOpenAt(startDate.time)
            .setEnd(now.time)
            .setValidator(DateValidatorPointBackward.now())

        MaterialDatePicker.Builder.datePicker()
            .setTitleText(getString(R.string.select_a_date))
            .setSelection(startDate.time)
            .setCalendarConstraints(constraintsBuilder.build())
            .build()
            .apply {
                addOnPositiveButtonClickListener {
                    reportsViewModel.setSelectedDate(it)
                }
            }
            .show(childFragmentManager, "DATE_PICKER")
    }

    private fun subscribeObservers() {
        reportsViewModel.reports.observe(viewLifecycleOwner) {
            it.onSuccess {
                Log.e("TAG", "subscribeObservers: $it")
                binding.currentPastPlanName.text = it.dailyPlan?.name ?: ""
                reportsAdapter.submitList(
                    it.dailyPlan?.exercises
                )
            }.onError {
                when (it) {
                    ReportsViewModel.NOT_FOUND_EXCEPTION ->
                        binding.errorMessage.text = getString(R.string.no_report_found)
                    else ->
                        binding.errorMessage.text = getString(R.string.we_couldn_t_load_your_report)
                }
            }
        }
    }
}
