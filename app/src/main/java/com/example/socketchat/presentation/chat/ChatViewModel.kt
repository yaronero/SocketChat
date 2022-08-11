package com.example.socketchat.presentation.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socketchat.data.dtomodels.wrappers.MessageWrapper
import com.example.socketchat.domain.ConnectionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChatViewModel(
    private val repository: ConnectionRepository,
    private val anotherUserId: String
) : ViewModel() {

    private val _newMessages = MessageSingleLiveEvent()
    val newMessages: LiveData<MessageWrapper>
        get() = _newMessages

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.newMessage.collect {
                if (it.messageDto.message != "")
                    _newMessages.postValue(it)
            }
        }
    }

    fun sendMessage(message: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.sendMessage(anotherUserId, message)
        }
    }

    fun getId(): String {
        return repository.getId() ?: ""
    }

    fun getUsername(): String {
        return repository.getUsername()
    }
}