package com.oguzhanaslann.domain_profile.domain.mapper

import com.oguzhanaslann.common.Mapper
import com.oguzhanaslann.common_data.local.room.entity.RecipeEntity
import com.oguzhanaslann.domain_profile.domain.model.FavoriteRecipe

class RecipeEntityToFavoriteRecipeMapper : Mapper<com.oguzhanaslann.common_data.local.room.entity.RecipeEntity, FavoriteRecipe> {
    override suspend fun map(input: com.oguzhanaslann.common_data.local.room.entity.RecipeEntity): FavoriteRecipe {
        return FavoriteRecipe(
            id = "${input.id}",
            name = input.title,
            imageUrl = input.imageUrl,
        )
    }
}
