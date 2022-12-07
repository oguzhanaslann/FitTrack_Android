package com.oguzhanaslann.feature_reports

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.oguzhanaslann.common.onSuccess
import com.oguzhanaslann.commonui.verticalLinearLayoutManaged
import com.oguzhanaslann.commonui.viewBinding
import com.oguzhanaslann.feature_reports.databinding.FragmentReportsBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

data class ReportExercise(
    val id: Int,
    val name: String,
    val description: String,
    val image: String,
    val isDone: Boolean,
)

data class ReportDailyPlan(
    val name: String,
    val exercises: List<ReportExercise>,
)

data class Report(
    val date: Date,
    val dailyPlan: ReportDailyPlan?,
)

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
        val date = reportsViewModel.getSelectedDateOrNow()
        val now = Date()
        val constraintsBuilder = CalendarConstraints.Builder()
            .setOpenAt(date.time)
            .setEnd(now.time)
            .setValidator(DateValidatorPointBackward.now())

        MaterialDatePicker.Builder.datePicker()
            .setTitleText(getString(R.string.select_a_date))
            .setSelection(date.time)
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
                reportsAdapter.submitList(it.dailyPlan?.exercises)
            }
        }
    }
}
