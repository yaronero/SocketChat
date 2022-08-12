package com.example.socketchat.presentation.userslist.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.socketchat.domain.data.dtomodels.User

class UsersListDiffCallback : DiffUtil.ItemCallback<User>() {

    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }
}