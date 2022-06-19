package com.deva.bangkit_submission1.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.deva.bangkit_submission1.R
import com.deva.bangkit_submission1.data.local.entity.UserEntity
import com.deva.bangkit_submission1.databinding.ItemRowUserBinding
import com.deva.bangkit_submission1.helper.UserDiffCallback
import com.deva.bangkit_submission1.ui.detail.DetailUserActivity

class FavoriteAdapter: RecyclerView.Adapter<FavoriteAdapter.UserViewHolder>() {

    private val listUsers = ArrayList<UserEntity>()
    fun setListUser(listUsers: List<UserEntity>) {
        val diffCallback = UserDiffCallback(this.listUsers, listUsers)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listUsers.clear()
        this.listUsers.addAll(listUsers)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(listUsers[position])
    }

    override fun getItemCount(): Int = listUsers.size

    inner class UserViewHolder (private val binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UserEntity) {
            with(binding) {
                tvItemUsername.text = user.username
                tvItemName.text = user.name
                Glide.with(itemView.context)
                    .load(user.urlAvatar)
                    .placeholder(R.drawable.profile_placeholder)
                    .into(imgItemPhoto)
                content.setOnClickListener {
                    val intent = Intent(it.context, DetailUserActivity::class.java)
                    intent.putExtra(DetailUserActivity.EXTRA_USERNAME, user.username)
                    intent.putExtra(DetailUserActivity.EXTRA_FAVORITE, user.isFavorited)
                    it.context.startActivity(intent)
                }
            }
        }
    }
}