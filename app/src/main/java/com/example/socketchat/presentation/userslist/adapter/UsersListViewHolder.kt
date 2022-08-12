package com.example.socketchat.presentation.userslist.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.socketchat.domain.data.dtomodels.User
import com.example.socketchat.databinding.UserItemBinding

class UsersListViewHolder(
    private val binding: UserItemBinding,
    private val onItemClickListener: (User) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(user: User) {
        binding.userName.text = user.name
        binding.root.setOnClickListener {
            onItemClickListener(user)
        }
    }
}