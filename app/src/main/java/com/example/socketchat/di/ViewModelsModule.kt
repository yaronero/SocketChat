package com.example.socketchat.di

import com.example.socketchat.presentation.authorization.AuthorizationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelsModule = module {

    viewModel {
        AuthorizationViewModel(get())
    }
}