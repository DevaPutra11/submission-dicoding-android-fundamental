package com.deva.bangkit_submission1.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.deva.bangkit_submission1.data.local.entity.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getUsers(): LiveData<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertUsers(user: UserEntity)

    @Update
    fun updateUsers(user: UserEntity)

    @Query("DELETE FROM user WHERE username = :username")
    fun deleteUsers(username:String)

    @Query("SELECT count(*) FROM user WHERE user.username = :username")
    suspend fun checkUser(username: String):Int
}