package com.deva.bangkit_submission1.helper

import androidx.recyclerview.widget.DiffUtil
import com.deva.bangkit_submission1.data.local.entity.UserEntity

class UserDiffCallback (private val mOldUserList: List<UserEntity>, private val mNewUserList: List<UserEntity>) : DiffUtil.Callback(){
    override fun getOldListSize(): Int {
        return mOldUserList.size
    }

    override fun getNewListSize(): Int {
        return mNewUserList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldUserList[oldItemPosition].username == mNewUserList[newItemPosition].username
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldEmployee = mOldUserList[oldItemPosition]
        val newEmployee = mNewUserList[newItemPosition]
        return oldEmployee.name == newEmployee.name && oldEmployee.company == newEmployee.company
    }
}