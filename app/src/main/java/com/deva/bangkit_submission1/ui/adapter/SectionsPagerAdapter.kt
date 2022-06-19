package com.deva.bangkit_submission1.ui.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.deva.bangkit_submission1.ui.UserFollowFragment

class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    var appUsername:String = ""

    override fun createFragment(position: Int): Fragment {
        val fragment = UserFollowFragment()
        fragment.arguments = Bundle().apply {
            putInt(UserFollowFragment.ARG_SECTION_NUMBER, position + 1)
            putString(UserFollowFragment.ARG_USERNAME, appUsername)
        }
        return fragment

    }

    override fun getItemCount(): Int {
        return 2
    }
}