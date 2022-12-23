package com.oguzhanaslann.domain_profile.domain.mapper

import com.oguzhanaslann.common.Mapper
import com.oguzhanaslann.commonui.data.local.room.entity.RecipeEntity
import com.oguzhanaslann.domain_profile.domain.model.FavoriteRecipe

class RecipeEntityToFavoriteRecipeMapper : Mapper<RecipeEntity, FavoriteRecipe> {
    override suspend fun map(input: RecipeEntity): FavoriteRecipe {
        return FavoriteRecipe(
            id = "${input.id}",
            name = input.title,
            imageUrl = input.imageUrl,
        )
    }
}
