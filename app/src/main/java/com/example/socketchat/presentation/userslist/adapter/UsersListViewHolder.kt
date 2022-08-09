package com.example.socketchat.presentation.userslist.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.socketchat.data.dtomodels.User
import com.example.socketchat.databinding.UserItemBinding

class UsersListViewHolder(
    private val binding: UserItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(user: User) {
        binding.userName.text = user.name
    }
}