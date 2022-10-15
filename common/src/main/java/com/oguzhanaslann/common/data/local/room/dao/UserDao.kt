package com.oguzhanaslann.common.data.local.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.oguzhanaslann.common.data.local.room.entity.UserEntity

@Dao
interface UserDao : BaseDao<UserEntity> {

    @Query("SELECT * FROM user WHERE email = :email")
    suspend fun getUserByEmail(email: String): UserEntity?
    // insert into user (email,password) values (email,password)
}
