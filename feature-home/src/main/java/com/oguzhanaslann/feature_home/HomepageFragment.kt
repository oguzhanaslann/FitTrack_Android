package com.oguzhanaslann.feature_home

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.load
import com.oguzhanaslann.commonui.viewBinding
import com.oguzhanaslann.feature_home.databinding.FragmentHomepageBinding
import com.oguzhanaslann.feature_home.domain.model.DailyWorkoutOverview
import dagger.hilt.android.AndroidEntryPoint
import java.text.MessageFormat
import kotlin.math.min

@AndroidEntryPoint
class HomepageFragment : Fragment(R.layout.fragment_homepage) {

    private val binding by viewBinding(FragmentHomepageBinding::bind)

    private val viewModel: HomepageViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeObserver()
    }

    private fun subscribeObserver() {
        viewModel.homepageUIState.observe(viewLifecycleOwner) { profileShortInfo ->
            setGreetingsCard(profileShortInfo.profileShortInfo)
            setTodaysWorkoutOverview(profileShortInfo.todaysWorkoutOverview)
        }
    }

    private fun setGreetingsCard(profileShortInfo: ProfileShortInfoUIState?) {
        binding.greetingCard.isVisible = profileShortInfo != null
        if (profileShortInfo != null) {
            binding.userProfileImage.load(profileShortInfo.profileShortInfo.userPhotoUrl) {
                error(com.oguzhanaslann.commonui.R.drawable.placeholder_thumbnail)
            }
            binding.userName.text = profileShortInfo.profileShortInfo.userFullName
            binding.currentDateText.text = profileShortInfo.currentDateText
        }
    }

    private fun setTodaysWorkoutOverview(todaysWorkoutOverview: DailyWorkoutOverview?) {
        binding.todaysDailyWorkoutCard.isVisible = todaysWorkoutOverview != null
        if (todaysWorkoutOverview != null) {
            binding.dailyWorkoutName.text = todaysWorkoutOverview.name
            binding.dailyWorkoutCaloriesText.text = binding.root.context.getString(
                com.oguzhanaslann.commonui.R.string.calories_format_text,
                todaysWorkoutOverview.calories
            )
            binding.dailyWorkoutProgress.progress =  min(todaysWorkoutOverview.progress, 100)
            binding.dailyWorkoutPercentageText.text = MessageFormat.format(
                getString(com.oguzhanaslann.commonui.R.string.percentage),
                todaysWorkoutOverview.progress
            )

            binding.dailyWorkoutImage.load(todaysWorkoutOverview.imageUrl) {
                error(com.oguzhanaslann.commonui.R.drawable.placeholder_thumbnail)
            }

        }
    }
}
