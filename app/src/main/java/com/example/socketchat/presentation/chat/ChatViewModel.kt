package com.example.socketchat.presentation.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socketchat.data.dtomodels.MessageDto
import com.example.socketchat.data.dtomodels.User
import com.example.socketchat.data.dtomodels.wrappers.MessageWrapper
import com.example.socketchat.domain.ConnectionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class ChatViewModel(
    private val repository: ConnectionRepository,
    private val anotherUserId: String
) : ViewModel() {

    private val _newMessages = MutableLiveData<List<MessageWrapper>>()
    val newMessages: LiveData<List<MessageWrapper>>
        get() = _newMessages

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.newMessage.collect {
                if (it.messageDto.message != "") {
                    val list = newMessages.value?.plus(it) ?: listOf(it)
                    _newMessages.postValue(list)
                }
            }
        }
    }

    fun sendMessage(message: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (message.isNotBlank()) {
                val messageModel = MessageWrapper(
                    UUID.randomUUID().toString(),
                    MessageDto(User(getId(), getUsername()), message)
                )
                val list = newMessages.value?.plus(messageModel) ?: listOf(messageModel)
                _newMessages.postValue(list)
            }
            repository.sendMessage(anotherUserId, message)
        }
    }

    private fun getId(): String {
        return repository.getId() ?: ""
    }

    private fun getUsername(): String {
        return repository.getUsername()
    }
}