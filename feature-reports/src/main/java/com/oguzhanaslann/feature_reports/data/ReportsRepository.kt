package com.oguzhanaslann.feature_reports.data

import com.oguzhanaslann.common.DateHelper
import com.oguzhanaslann.common.Mapper
import com.oguzhanaslann.commonui.data.local.FitTrackDataStore
import com.oguzhanaslann.commonui.data.local.room.dao.UserDailyPlanDao
import com.oguzhanaslann.commonui.data.local.room.entity.UserDailyPlanWithExercises
import com.oguzhanaslann.feature_reports.domain.Report
import com.oguzhanaslann.feature_reports.domain.ReportDailyPlan
import com.oguzhanaslann.feature_reports.domain.ReportExercise
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import java.util.*

interface ReportsRepository {
    /**
     * Returns a list of reports for the given user id.
     * @param date Date to get the reports for. The dates clock time will be ignored.
     * @return Result<Report?>
     */
    suspend fun getReportOnDate(date: Date): Result<Report?>
}

fun ReportsRepository(
    userDailyPlanDao: UserDailyPlanDao,
    fitTrackDataStore: FitTrackDataStore,
    reportMapper: Mapper<UserDailyPlanWithExercises, Report>,
) = object : ReportsRepository {
    override suspend fun getReportOnDate(date: Date): Result<Report?> {
        val userId = fitTrackDataStore.getUserId().first()

        val startOfTheDate = DateHelper.getStartDateOf(date)

        val userDailyPlanWithExercisesList =
            userDailyPlanDao.getCompletedUserDailyPlanWithExercisesByDate(userId, startOfTheDate.time).orEmpty()

        val userDailyPlanWithExercises = userDailyPlanWithExercisesList.firstOrNull()

        return userDailyPlanWithExercises?.let { reportMapper.map(it) }?.let { Result.success(it) }
            ?: Result.success(null)
    }
}
