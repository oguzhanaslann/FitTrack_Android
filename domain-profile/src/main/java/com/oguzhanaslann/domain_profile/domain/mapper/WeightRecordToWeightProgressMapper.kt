package com.oguzhanaslann.domain_profile.domain.mapper

import com.oguzhanaslann.common.Mapper
import com.oguzhanaslann.common_data.local.room.entity.WeightRecordEntity
import com.oguzhanaslann.domain_profile.domain.model.WeightProgress
import java.util.*

class WeightRecordToWeightProgressMapper : Mapper<com.oguzhanaslann.common_data.local.room.entity.WeightRecordEntity, WeightProgress> {
    override suspend fun map(input: com.oguzhanaslann.common_data.local.room.entity.WeightRecordEntity): WeightProgress {
        return WeightProgress(
            weight = input.weight,
            date = run {
                val dateMillis = input.date
                Date(dateMillis)
            }
        )
    }
}
