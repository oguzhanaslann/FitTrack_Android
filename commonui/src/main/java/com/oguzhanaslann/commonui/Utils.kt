package com.oguzhanaslann.commonui

import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.delay

fun attachTabsWithPager(
    tabLayout: TabLayout,
    viewPager: ViewPager2,
    configureTab: (tab: TabLayout.Tab, position: Int) -> Unit = { _, _ -> }
) {
    val mediator = TabLayoutMediator(tabLayout, viewPager, configureTab)
    mediator.attach()
}


suspend fun <T> withAtLeastDurationOf(millis: Long, block: suspend () -> T): T {
    val now = System.currentTimeMillis()
    val result = block()
    val duration = System.currentTimeMillis() - now
    if (duration < millis) {
        delay(millis - duration)
    }
    return result
}
