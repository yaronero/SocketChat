package com.example.socketchat.presentation.chat.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.socketchat.data.dtomodels.wrappers.MessageWrapper

class ChatDiffUtilCallback : DiffUtil.ItemCallback<MessageWrapper>() {

    override fun areItemsTheSame(oldItem: MessageWrapper, newItem: MessageWrapper): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MessageWrapper, newItem: MessageWrapper): Boolean {
        return oldItem == newItem
    }
}