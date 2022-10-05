package com.oguzhanaslann.commonui

import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

fun attachTabsWithPager(
    tabLayout: TabLayout,
    viewPager: ViewPager2,
    configureTab: (tab: TabLayout.Tab, position: Int) -> Unit = { _, _ -> }
) {
    val mediator = TabLayoutMediator(tabLayout, viewPager, configureTab)
    mediator.attach()
}
