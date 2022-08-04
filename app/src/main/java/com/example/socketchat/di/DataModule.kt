package com.example.socketchat.di

import com.example.socketchat.data.ConnectRepositoryImpl
import com.example.socketchat.domain.ConnectRepository
import org.koin.dsl.module

val dataModule = module {

    factory<ConnectRepository> {
        ConnectRepositoryImpl()
    }
}