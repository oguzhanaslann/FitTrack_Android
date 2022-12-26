package com.oguzhanaslann.feature_reports.domain.mapper

import com.oguzhanaslann.common.Mapper
import com.oguzhanaslann.common_data.local.room.entity.UserDailyPlanWithExercises
import com.oguzhanaslann.feature_reports.domain.Report
import com.oguzhanaslann.feature_reports.domain.ReportDailyPlan
import com.oguzhanaslann.feature_reports.domain.ReportExercise
import java.util.*

class ReportMapper : Mapper<UserDailyPlanWithExercises, Report> {
    override suspend fun map(input: UserDailyPlanWithExercises): Report {
        return Report(
            date = Date(input.userDailyPlanEntity.endDate),
            dailyPlan = ReportDailyPlan(
                name = input.userDailyPlanEntity.name,
                exercises = input.exercises.map {
                    ReportExercise(
                        id = it.id,
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
