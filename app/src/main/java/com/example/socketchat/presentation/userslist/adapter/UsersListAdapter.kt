package com.example.socketchat.presentation.userslist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.socketchat.models.dtomodels.User
import com.example.socketchat.databinding.UserItemBinding

class UsersListAdapter(
    private val onItemClickListener: (User) -> Unit
) : ListAdapter<User, UsersListViewHolder>(UsersListDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersListViewHolder {
        val binding = UserItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return UsersListViewHolder(binding, onItemClickListener)
    }

    override fun onBindViewHolder(holder: UsersListViewHolder, position: Int) {
        val user = currentList[position]
        holder.bind(user)
    }
}