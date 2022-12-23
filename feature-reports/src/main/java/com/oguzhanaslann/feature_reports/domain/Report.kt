package com.oguzhanaslann.feature_reports.domain

import java.util.*

data class Report(
    val date: Date,
    val dailyPlan: ReportDailyPlan?,
)
