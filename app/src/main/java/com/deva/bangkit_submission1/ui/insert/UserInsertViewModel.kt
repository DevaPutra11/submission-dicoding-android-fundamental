package com.deva.bangkit_submission1.ui.insert

import android.app.Application
import androidx.lifecycle.ViewModel
import com.deva.bangkit_submission1.data.UserRepository
import com.deva.bangkit_submission1.data.local.entity.UserEntity

class UserInsertViewModel(application: Application) : ViewModel() {
    private val mUserRepository:UserRepository = UserRepository(application)

    fun insert(user:UserEntity){
        mUserRepository.insert(user)
    }

    fun saveUser(user: UserEntity){
        mUserRepository.setFavorited(user,true)
    }

    fun deleteUser(user: UserEntity){
        mUserRepository.setFavorited(user,false)
    }

    suspend fun checkUser(username:String):Int = mUserRepository.checkUser(username)

    fun delete(user: UserEntity){
        mUserRepository.delete(user)
    }
}