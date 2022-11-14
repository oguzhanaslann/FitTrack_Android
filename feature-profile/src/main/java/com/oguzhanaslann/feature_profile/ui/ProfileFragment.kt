package com.oguzhanaslann.feature_profile.ui

import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.oguzhanaslann.common.onSuccess
import com.oguzhanaslann.commonui.dp
import com.oguzhanaslann.commonui.horizontalLinearLayoutManaged
import com.oguzhanaslann.commonui.themeColor
import com.oguzhanaslann.commonui.viewBinding
import com.oguzhanaslann.feature_profile.R
import com.oguzhanaslann.feature_profile.databinding.FragmentProfileBinding
import com.oguzhanaslann.feature_profile.domain.FavoriteRecipe
import com.oguzhanaslann.feature_profile.domain.OldWorkoutPlanOverView
import com.oguzhanaslann.feature_profile.domain.ProgressPhoto
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val binding by viewBinding(FragmentProfileBinding::bind)
    private val profileViewModel: ProfileViewModel by viewModels()

    private val progressPhotoAdapter by lazy {
        ProgressPhotoAdapter(
            onAddPhotoClick = ::onAddPhotoClick,
            onFirstItemBound = ::addItemMarginToFirstItem
        )
    }

    private val takePicturePreview =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) {
            // todo handle image
        }

    private val oldWorkoutPlanAdapter by lazy {
        OldWorkoutPlanAdapter(
            onSeeDetailsClick = ::onSeeDetailsClick
        )
    }

    private val favoriteRecipeAdapter by lazy {
        FavoriteRecipeAdapter()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileViewModel.getProfileUIState()
        subscribeObservers()
    }

    private fun subscribeObservers() {
        profileViewModel.profileUIState.observe(viewLifecycleOwner) {
            it.onSuccess {
                binding.userNameText.text = it.userName
                progressPhotoAdapter.submitList(it.progressPhotos)
                setWeightProgressChart(it)
                oldWorkoutPlanAdapter.submitList(it.oldWorkouts)
                favoriteRecipeAdapter.submitList(it.favoriteRecipes)
                setActiveWorkoutPlan(it)
            }
        }
    }

    private fun setActiveWorkoutPlan(it: ProfileUIState) {
        it.activeWorkoutPlan?.let {
            binding.activePlanLayout.activeWorkoutPlanName.text = it.name
            binding.activePlanLayout.activeWorkoutPlanDescription.text = it.description
            binding.activePlanLayout.activePlanProgress.progress = maxOf(it.progress, 100f)
        }
    }

    private fun setWeightProgressChart(it: ProfileUIState) {
        val entries = it.weight.map { it.toEntry() }
        val dataSet = LineDataSet(entries, "")
        dataSet.apply {
            this.color = binding.root.context.themeColor(R.attr.colorPrimaryProfile)
        }
        binding.weightLineChart.data = LineData(dataSet)
        binding.weightLineChart.invalidate()
    }

    private fun onAddPhotoClick() {
        takePicturePreview.launch(null)
    }

    private fun addItemMarginToFirstItem(view: View) {
        val layoutParams = view.layoutParams as? RecyclerView.LayoutParams
        layoutParams?.let {
            it.marginStart = 8.dp
            view.layoutParams = layoutParams
        }
    }

    private fun onSeeDetailsClick(oldWorkoutPlanOverView: OldWorkoutPlanOverView) {
        //  TODO("Not yet implemented")
    }
}
