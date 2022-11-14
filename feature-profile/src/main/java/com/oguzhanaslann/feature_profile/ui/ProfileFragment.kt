package com.oguzhanaslann.feature_profile.ui

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.oguzhanaslann.commonui.dp
import com.oguzhanaslann.commonui.horizontalLinearLayoutManaged
import com.oguzhanaslann.commonui.themeColor
import com.oguzhanaslann.commonui.viewBinding
import com.oguzhanaslann.feature_profile.R
import com.oguzhanaslann.feature_profile.databinding.FragmentProfileBinding
import com.oguzhanaslann.feature_profile.domain.ProgressPhoto

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val binding by viewBinding(FragmentProfileBinding::bind)

    private val progressPhotoAdapter by lazy {
        ProgressPhotoAdapter(
            onAddPhotoClick = ::onAddPhotoClick,
            firstItemPadding = 8.dp
        )
    }

    private val takePicturePreview =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) {
            // todo handle image
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.apply {
            horizontalLinearLayoutManaged()

            adapter = progressPhotoAdapter

            progressPhotoAdapter.submitList(
                listOf(
                    ProgressPhoto("1", "url", "description"),
                    ProgressPhoto("2", "url", "description"),
                    ProgressPhoto("3", "url", "description"),
                )
            )
        }

        binding.weightLineChart.apply {
            val entries = listOf(
                Entry(0f, 70f),
                Entry(1f, 75f),
                Entry(2f, 80f),
            )

            val dataSet = LineDataSet(entries, "")

            dataSet.apply {
                this.color = context.themeColor(R.attr.colorPrimaryProfile)
            }

            data = LineData(dataSet)
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

    }

    private fun onAddPhotoClick() {
        takePicturePreview.launch(null)
    }
}
