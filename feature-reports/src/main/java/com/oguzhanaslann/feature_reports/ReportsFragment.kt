package com.oguzhanaslann.feature_reports

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.oguzhanaslann.commonui.verticalLinearLayoutManaged
import com.oguzhanaslann.commonui.viewBinding
import com.oguzhanaslann.feature_reports.databinding.FragmentReportsBinding
import com.oguzhanaslann.feature_reports.databinding.ItemExerciseLayoutBinding
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

class ReportExerciseAdapter :
    ListAdapter<ReportExercise, ReportExerciseAdapter.Holder>(DiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemExerciseLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val currentItem = getItem(position)
        holder.onBind(currentItem)
    }

    inner class Holder(val binding: ItemExerciseLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(currentItem: ReportExercise) = binding.run {
            exerciseImage.load(currentItem.image) {
                placeholder(com.oguzhanaslann.commonui.R.drawable.placeholder_thumbnail)
                error(com.oguzhanaslann.commonui.R.drawable.placeholder_thumbnail)
            }

            exerciseNameText.text = currentItem.name
            exerciseDescriptionText.text = currentItem.description
            exerciseDoneImage.isVisible = currentItem.isDone
        }
    }

    class DiffCallBack : DiffUtil.ItemCallback<ReportExercise>() {
        override fun areItemsTheSame(oldItem: ReportExercise, newItem: ReportExercise): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ReportExercise, newItem: ReportExercise): Boolean =
            oldItem == newItem
    }
}

@AndroidEntryPoint
class ReportsFragment : Fragment(R.layout.fragment_reports) {

    private val binding by viewBinding(FragmentReportsBinding::bind)

    private val reportsViewModel: ReportsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = reportsViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        initViews()
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
            adapter = ReportExerciseAdapter().apply {
                submitList(
                    listOf(
                        ReportExercise(
                            1,
                            "name",
                            "description",
                            "https://www.google.com/images/branding/googlelogo/2x/googlelogo_color_272x92dp.png",
                            true
                        ),
                        ReportExercise(
                            2,
                            "name",
                            "description",
                            "https://www.google.com/images/branding/googlelogo/2x/googlelogo_color_272x92dp.png",
                            false
                        ),
                        ReportExercise(
                            3,
                            "name",
                            "description",
                            "https://www.google.com/images/branding/googlelogo/2x/googlelogo_color_272x92dp.png",
                            true
                        ),
                        ReportExercise(
                            4,
                            "name",
                            "description",
                            "https://www.google.com/images/branding/googlelogo/2x/googlelogo_color_272x92dp.png",
                            false
                        ),
                    )
                )
            }
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
}
