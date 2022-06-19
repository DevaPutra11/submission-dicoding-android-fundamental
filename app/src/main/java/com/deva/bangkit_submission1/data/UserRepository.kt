package com.deva.bangkit_submission1.data

import android.app.Application
import androidx.lifecycle.LiveData
import com.deva.bangkit_submission1.data.local.entity.UserEntity
import com.deva.bangkit_submission1.data.local.room.UserDao
import com.deva.bangkit_submission1.data.local.room.UserDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class UserRepository(application: Application) {
    private val mUserDao:UserDao
    private val executorService:ExecutorService=Executors.newSingleThreadExecutor()

    init {
        val db = UserDatabase.getDatabase(application)
        mUserDao = db.userDao()
    }

    fun getAllUser():LiveData<List<UserEntity>> = mUserDao.getUsers()

    fun insert(user:UserEntity){
        executorService.execute { mUserDao.insertUsers(user) }
    }

    fun delete(user: UserEntity){
        executorService.execute { mUserDao.deleteUsers(user.username) }
    }

    suspend fun checkUser(username:String):Int {
        return mUserDao.checkUser(username)
    }

    fun setFavorited(user: UserEntity, state:Boolean){
        executorService.execute {
            user.isFavorited = state
            mUserDao.updateUsers(user)
        }
    }
}