package com.example.socketchat.di

import com.example.socketchat.presentation.authorization.AuthorizationViewModel
import com.example.socketchat.presentation.chat.ChatViewModel
import com.example.socketchat.presentation.userslist.UsersListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelsModule = module {

    viewModel {
        AuthorizationViewModel(get())
    }

    viewModel {
        UsersListViewModel(get())
    }

    viewModel { parameters ->
        ChatViewModel(get(), get())
    }
}