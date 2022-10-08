package com.oguzhanaslann.feature_onboard

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class OnboardingPagerAdapter(
    private val context: Context,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return PAGER_PAGE_COUNT
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            PAGER_PAGE_FIRST -> {
                OnboardingPageFragment.newInstance(
                    pageTitle = context.getString(R.string.onboard_page_title_1),
                    pageDescription = context.getString(R.string.onboard_page_description_1),
                    pageImage = R.drawable.figure_running_woman
                )
            }

            PAGER_PAGE_SECOND -> {
                OnboardingPageFragment.newInstance(
                    pageTitle = context.getString(R.string.onboard_page_title_2),
                    pageDescription = context.getString(R.string.onboard_page_description_2),
                    pageImage = R.drawable.figure_lifting_woman
                )
            }

            PAGER_PAGE_THIRD -> {
                OnboardingPageFragment.newInstance(
                    pageTitle = context.getString(R.string.onboard_page_title_3),
                    pageDescription = context.getString(R.string.onboard_page_description_3),
                    pageImage = R.drawable.figure_man_woman
                )
            }

            else -> error("Invalid position")
        }
    }

    companion object {
        const val PAGER_PAGE_COUNT = 3
        const val PAGER_PAGE_FIRST = 0
        const val PAGER_PAGE_SECOND = 1
        const val PAGER_PAGE_THIRD = 2

    }
}
