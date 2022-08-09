package com.example.socketchat.presentation.userslist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.socketchat.data.dtomodels.User
import com.example.socketchat.databinding.UserItemBinding

class UsersListAdapter() : ListAdapter<User, UsersListViewHolder>(UsersListDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersListViewHolder {
        val binding = UserItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return UsersListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UsersListViewHolder, position: Int) {
        val user = currentList[position]
        holder.bind(user)
    }
}