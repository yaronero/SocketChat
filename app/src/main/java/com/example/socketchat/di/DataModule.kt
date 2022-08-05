package com.example.socketchat.di

import com.example.socketchat.data.repository.ConnectionRepositoryImpl
import com.example.socketchat.domain.ConnectionRepository
import org.koin.dsl.module


val dataModule = module {

    single<ConnectionRepository> {
        ConnectionRepositoryImpl()
    }
}