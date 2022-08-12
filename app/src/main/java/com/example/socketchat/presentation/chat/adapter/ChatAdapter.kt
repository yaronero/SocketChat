package com.example.socketchat.presentation.chat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.socketchat.models.wrappers.MessageWrapper
import com.example.socketchat.databinding.AnotherUserMessageBinding
import com.example.socketchat.databinding.MyMessageBinding

class ChatAdapter(
    private val anotherUserId: String
) : ListAdapter<MessageWrapper, ChatViewHolder>(ChatDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        return if (viewType == VIEW_TYPE_MY_MESSAGE)
            MyMessageViewHolder(
                MyMessageBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        else
            AnotherUserMessageViewHolder(
                AnotherUserMessageBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val message = currentList[position]
        holder.bind(message)
    }

    override fun getItemViewType(position: Int): Int {
        return if (currentList[position].messageDto.from.id == anotherUserId)
            VIEW_TYPE_ANOTHER_USER_MESSAGE
        else
            VIEW_TYPE_MY_MESSAGE
    }

    companion object {
        private const val VIEW_TYPE_MY_MESSAGE = 0
        private const val VIEW_TYPE_ANOTHER_USER_MESSAGE = 1
    }
}