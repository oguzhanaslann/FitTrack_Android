package com.oguzhanaslann.feature_create_workout.domain.usecase

import android.content.Context
import android.net.Uri
import com.oguzhanaslann.commonui.copyTo
import com.oguzhanaslann.commonui.createDirIfNotExist
import com.oguzhanaslann.commonui.createIfNotExist
import com.oguzhanaslann.commonui.toUrlString
import java.io.File
import java.util.*

class WorkoutLocalPhotosUseCase(
    private val applicationContext: Context,
) {

    fun saveWorkoutPhotoAndGetUrl(workoutName: String, uri: Uri): String {
        val workoutPhotoDir = createTopMostWorkoutPhotosDirIfNotExists(workoutName)
        val workoutPhotoFile = createWorkoutPhotoDirIfNotExists(workoutPhotoDir, workoutName)
        uri.copyTo(workoutPhotoFile, applicationContext)
        return workoutPhotoFile.toUrlString()
    }

    private fun createWorkoutPhotoDirIfNotExists(
        workoutPhotoDir: File,
        workoutName: String,
    ): File {
        val workoutPhotoFile = File(workoutPhotoDir, workoutCoverPhotoName(workoutName))
        workoutPhotoFile.createIfNotExist()
        return workoutPhotoFile
    }

    private fun createTopMostWorkoutPhotosDirIfNotExists(workoutName: String): File {
        val workoutPhotosDir = createAndGetTopMostParentDirIfNeeded()
        val workoutPhotoDir = File(workoutPhotosDir, workoutPhotoDirName(workoutName))
        workoutPhotoDir.createDirIfNotExist()
        return workoutPhotoDir
    }

    private fun createAndGetTopMostParentDirIfNeeded(): File {
        val workoutPhotoFile = File(applicationContext.filesDir, WORKOUT_IMAGES_DIR)
        workoutPhotoFile.createDirIfNotExist()
        return workoutPhotoFile
    }

    private fun workoutPhotoDirName(workoutName: String): String {
        return "${workoutName}_photos/"
    }

    private fun workoutCoverPhotoName(workoutName: String): String {
        return "${workoutName}_cover.jpg"
    }

    fun saveDailyPlanPhotoAndGetUrl(
        workoutName: String,
        dailyPlanName: String,
        uri: Uri,
    ): String {
        val workoutPhotoDir = createTopMostWorkoutPhotosDirIfNotExists(workoutName)
        val dailyPhotoFile = createDailyPlanPhotoDirIfNotExists(
            workoutPhotoDir = workoutPhotoDir,
            workoutName = dailyPlanName,
            dailyPlanName = dailyPlanName
        )
        uri.copyTo(dailyPhotoFile, applicationContext)
        return dailyPhotoFile.toUrlString()
    }

    private fun createDailyPlanPhotoDirIfNotExists(
        workoutPhotoDir: File,
        workoutName: String,
        dailyPlanName: String,
    ): File {
        val workoutPhotoFile = File(workoutPhotoDir, dailyPlanPhotoName(workoutName, dailyPlanName))
        workoutPhotoFile.createIfNotExist()
        return workoutPhotoFile
    }

    private fun dailyPlanPhotoName(workoutName: String, dailyPlanName: String): String {
        return "${workoutName}_${dailyPlanName}_${UUID.randomUUID()}.jpg"
    }

    companion object {
        // workout images dirs name
        const val WORKOUT_IMAGES_DIR = "workout_images/"
    }
}
