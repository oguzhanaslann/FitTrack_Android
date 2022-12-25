package com.oguzhanaslann.domain_profile.domain.mapper

import com.oguzhanaslann.common.DateHelper
import com.oguzhanaslann.common.Mapper
import com.oguzhanaslann.common_data.local.room.entity.ProgressionPhotoEntity
import com.oguzhanaslann.domain_profile.domain.model.ProgressPhoto
import java.util.*

class ProgressionPhotoToProgressPhotoMapper : Mapper<com.oguzhanaslann.common_data.local.room.entity.ProgressionPhotoEntity, ProgressPhoto> {
    override suspend fun map(input: com.oguzhanaslann.common_data.local.room.entity.ProgressionPhotoEntity): ProgressPhoto {
        return ProgressPhoto(
            id = input.photoUrl,
            url = input.photoUrl,
            description = run {
                val dateMillis = input.date
                val date = Date(dateMillis)
                DateHelper.format(
                    date,
                    DateHelper.DAY_MONTH_WITH_NAME_YEAR_FORMAT,
                    autoLocale = true
                )
            }
        )
    }
}
