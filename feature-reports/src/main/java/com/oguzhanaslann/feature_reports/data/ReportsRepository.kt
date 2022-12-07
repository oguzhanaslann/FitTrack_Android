package com.oguzhanaslann.feature_reports.data

import com.oguzhanaslann.common.Mapper
import com.oguzhanaslann.commonui.data.local.FitTrackDataStore
import com.oguzhanaslann.commonui.data.local.room.dao.UserDailyPlanDao
import com.oguzhanaslann.commonui.data.local.room.entity.UserDailyPlanWithExercises
import com.oguzhanaslann.feature_reports.Report
import com.oguzhanaslann.feature_reports.ReportDailyPlan
import com.oguzhanaslann.feature_reports.ReportExercise
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import java.util.*

interface ReportsRepository {
    suspend fun getReportOnDate(date: Date): Result<Report?>
}

fun ReportsRepository(
    userDailyPlanDao: UserDailyPlanDao,
    fitTrackDataStore: FitTrackDataStore,
    reportMapper: Mapper<UserDailyPlanWithExercises, Report>,
) = object : ReportsRepository {
    override suspend fun getReportOnDate(date: Date): Result<Report?> {
        val userId = fitTrackDataStore.getUserId().first()
        val userDailyPlanWithExercisesList =
            userDailyPlanDao.getCompletedUserDailyPlanWithExercisesByDate(userId, date.time).orEmpty()
        val userDailyPlanWithExercises = userDailyPlanWithExercisesList.firstOrNull()
        return userDailyPlanWithExercises?.let { reportMapper.map(it) }?.let { Result.success(it) }
            ?: Result.success(null)
    }
}

class ReportMapper : Mapper<UserDailyPlanWithExercises, Report> {
    override suspend fun map(input: UserDailyPlanWithExercises): Report {
        return Report(
            date = Date(input.userDailyPlanEntity.endDate),
            dailyPlan = ReportDailyPlan(
                name = input.userDailyPlanEntity.name,
                exercises = input.exercises.map {
                    ReportExercise(
                        id = it.id ?: 0,
                        name = it.name,
                        description = it.description,
                        image = it.imageUrl,
                        isDone = it.isCompleted,
                    )
                }
            )
        )
    }
}
