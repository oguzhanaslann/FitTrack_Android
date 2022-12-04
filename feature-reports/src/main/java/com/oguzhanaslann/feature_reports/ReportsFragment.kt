package com.oguzhanaslann.feature_reports

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.oguzhanaslann.commonui.viewBinding
import com.oguzhanaslann.feature_reports.databinding.FragmentReportsBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class ReportsFragment : Fragment(R.layout.fragment_reports) {

    private val binding by viewBinding(FragmentReportsBinding::bind)

    private val reportsViewModel:ReportsViewModel  by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = reportsViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        initViews()
    }

    private fun initViews() = binding.run {
        dateNavigationContainer.setOnClickListener {
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

        moveForwardIcon.setOnClickListener {
            reportsViewModel.moveNextDayIfPossible()
        }

        moveBackwardsIcon.setOnClickListener {
            reportsViewModel.movePreviousDay()
        }
    }
}
