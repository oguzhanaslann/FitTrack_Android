package com.oguzhanaslann.domain_profile.domain.usecase

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.oguzhanaslann.commonui.copyTo
import com.oguzhanaslann.commonui.createIfNotExist
import com.oguzhanaslann.commonui.toUrlString
import com.oguzhanaslann.domain_profile.domain.ProfileRepository
import java.io.File
import java.util.*

typealias PhotoUrlAndLastEditDate = Pair<String, Date>

class LocalPhotosUseCase(
    private val applicationContext: Context,
    private val profileRepository: ProfileRepository
) {
    suspend fun saveAndUpdateProfilePhoto(uri: Uri) {
        val profilePhotoFile = File(applicationContext.filesDir, PROFILE_PHOTO_FILE_NAME)
        profilePhotoFile.createIfNotExist()
        uri.copyTo(profilePhotoFile, applicationContext)
        val url = profilePhotoFile.toUrlString()
        profileRepository.setUserProfilePhoto(url)
    }

    suspend fun saveProfilePhoto(uri: Uri): String {
        val profilePhotoFile = File(applicationContext.filesDir, PROFILE_PHOTO_FILE_NAME)
        profilePhotoFile.createIfNotExist()
        uri.copyTo(profilePhotoFile, applicationContext)
        val url = profilePhotoFile.toUrlString()
        return url
    }

    fun getNewProgressPhotoUri(): Uri {
        return generatePhotoUri("progress")
    }

    private fun generatePhotoUri(prefix: String): Uri {
        val progressPhotosDir = File(applicationContext.filesDir, PROGRESS_PHOTO_DIR_NAME)
        if (progressPhotosDir.exists().not()) {
            progressPhotosDir.mkdirs()
        }

        val photoFile = File(progressPhotosDir, "${prefix}_${UUID.randomUUID()}.jpg")
        if (photoFile.exists().not()) {
            photoFile.createNewFile()
        }

        return FileProvider.getUriForFile(
            applicationContext,
            applicationContext.packageName + ".provider",
            photoFile
        )
    }

    suspend fun updateProgressPhotos() {
        val progressPhotosDir = File(applicationContext.filesDir, PROGRESS_PHOTO_DIR_NAME)
        if (progressPhotosDir.exists().not()) {
            return
        }

        val progressPhotoUrlsWithLastEditDate = progressPhotosDir.listFiles()?.map { file ->
            val creationDate = Date(file.lastModified())
            file.toUrlString() to creationDate
        } ?: emptyList()

        profileRepository.updateProgressPhotos(progressPhotoUrlsWithLastEditDate)
    }

    companion object {
        const val PROFILE_PHOTO_FILE_NAME = "profile_photo.jpg"
        const val PROGRESS_PHOTO_DIR_NAME = "progress_photos/"
    }
}
