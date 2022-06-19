package com.deva.bangkit_submission1.ui

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.deva.bangkit_submission1.data.UserRepository
import com.deva.bangkit_submission1.data.local.entity.UserEntity

class UserViewModel(application: Application) : ViewModel() {
    private val mUserRepository:UserRepository = UserRepository(application)

    fun getAllUser():LiveData<List<UserEntity>> = mUserRepository.getAllUser()
}