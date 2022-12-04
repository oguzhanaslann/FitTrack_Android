package com.oguzhanaslann.feature_profile.ui

import android.os.Bundle
import android.view.View
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.oguzhanaslann.common.isTrue
import com.oguzhanaslann.common.onSuccess
import com.oguzhanaslann.commonui.dp
import com.oguzhanaslann.commonui.horizontalLinearLayoutManaged
import com.oguzhanaslann.commonui.showErrorSnackbar
import com.oguzhanaslann.commonui.showSuccessSnackbar
import com.oguzhanaslann.commonui.themeColor
import com.oguzhanaslann.commonui.viewBinding
import com.oguzhanaslann.feature_profile.R
import com.oguzhanaslann.feature_profile.databinding.FragmentProfileBinding
import com.oguzhanaslann.feature_profile.domain.model.OldWorkoutPlanOverView
import dagger.hilt.android.AndroidEntryPoint

val Fragment.TAG get() = this::class.java.simpleName

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
        registerForActivityResult(ActivityResultContracts.TakePicture()) {
            if (it.isTrue()) {
                showSuccessSnackbar(
                    container = binding.root,
                    message = "Photo added successfully",
                    onDialogDismissed = {}
                )
                profileViewModel.updateProgressPhotos()
            } else {
                showErrorSnackbar(
                    container = binding.root,
                    message = "Photo adding failed",
                    onDialogDismissed = {}
                )
            }
        }

    private val photoPicker =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri ?: return@registerForActivityResult
            profileViewModel.updateProfilePhoto(uri)
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
        binding.viewModel = profileViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        initUI()
        profileViewModel.getProfileUIState()
        subscribeObservers()
    }

    private fun initUI() {
        binding.weightLineChart.apply {
            xAxis.apply {
                setDrawAxisLine(true)
                setDrawGridLines(false)
                setDrawLabels(true)
                position = XAxis.XAxisPosition.BOTTOM
                setAvoidFirstLastClipping(true)
                setDrawLimitLinesBehindData(true)

                //                valueFormatter = object : ValueFormatter() {
                //                    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                //                        val day = stats[value.toInt()].first
                //                        return labelFactory(day)
                //                    }
                //                }
                //                typeface = Typeface.createFromAsset(requireContext().assets, "app/src/main/res/font/poppins_semi_bold.ttf")
            }

            axisLeft.apply {
                setDrawTopYLabelEntry(true)
                isEnabled = true
                setDrawAxisLine(true)
                setDrawGridLines(false)
                setDrawLabels(true)
                setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
                //                axisMinimum = 0f
            }
            axisRight.isEnabled = false

            setTouchEnabled(false)
            isDragEnabled = false
            isScaleYEnabled = false
            isScaleXEnabled = false
            legend.form = Legend.LegendForm.NONE
            description = Description().apply { text = "" }
            invalidate()
        }
        binding.profileEditPhotoButton.setOnClickListener {
            photoPicker.launch(
                PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.ImageOnly
                )
            )
        }
        binding.rvGallery.apply {
            horizontalLinearLayoutManaged()
            adapter = progressPhotoAdapter
        }

    }

    private fun subscribeObservers() {
        profileViewModel.profileUIState.observe(viewLifecycleOwner) {
            it.onSuccess {
                loadProfilePhoto(it.userProfile.profilePhotoUrl)
                progressPhotoAdapter.submitList(it.progressPhotos)
                setWeightProgressChart(it)
                oldWorkoutPlanAdapter.submitList(it.oldWorkouts)
                favoriteRecipeAdapter.submitList(it.favoriteRecipes)
                setActiveWorkoutPlan(it)
            }
        }
    }

    private fun loadProfilePhoto(url: String?) {
        binding.profileImage.load(url) {
            placeholder(com.oguzhanaslann.commonui.R.drawable.placeholder_thumbnail)
            error(com.oguzhanaslann.commonui.R.drawable.placeholder_thumbnail)
            lifecycle(viewLifecycleOwner)
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
        val entries = it.weightProgresses.map { it.toEntry() }
        val dataSet = LineDataSet(entries, "")
        dataSet.apply {
            this.color = binding.root.context.themeColor(R.attr.colorPrimaryProfile)
        }
        binding.weightLineChart.data = LineData(dataSet)
        binding.weightLineChart.invalidate()
    }

    private fun onAddPhotoClick() {
        takePicturePreview.launch(
            profileViewModel.getNewProgressPhotoUri()
        )
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
