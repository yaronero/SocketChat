package com.example.socketchat.presentation.chat.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.socketchat.data.dtomodels.wrappers.MessageWrapper
import com.example.socketchat.databinding.AnotherUserMessageBinding
import com.example.socketchat.databinding.MyMessageBinding

abstract class ChatViewHolder(
    view: View
) : RecyclerView.ViewHolder(view) {

    open fun bind(messageWrapper: MessageWrapper){}
}

class MyMessageViewHolder(
    private val binding: MyMessageBinding
) : ChatViewHolder(binding.root) {

    override fun bind(messageWrapper: MessageWrapper) {
        super.bind(messageWrapper)
        binding.message.text = messageWrapper.messageDto.message
    }
}

class AnotherUserMessageViewHolder(
    private val binding: AnotherUserMessageBinding
) : ChatViewHolder(binding.root) {

    override fun bind(messageWrapper: MessageWrapper) {
        super.bind(messageWrapper)
        binding.message.text = messageWrapper.messageDto.message
    }
}