package com.example.socketchat.domain.data.di

import android.content.Context
import com.example.socketchat.domain.data.repository.ConnectionRepositoryImpl
import com.example.socketchat.domain.data.repository.UserSharedPrefsRepositoryImpl
import com.example.socketchat.domain.domain.ConnectionRepository
import com.example.socketchat.domain.domain.UserSharedPrefsRepository
import com.example.socketchat.domain.utils.SHARED_PREFS_USER
import org.koin.dsl.module


val dataModule = module {

    single<ConnectionRepository> {
        ConnectionRepositoryImpl(get())
    }

    single {
        get<Context>().getSharedPreferences(SHARED_PREFS_USER, Context.MODE_PRIVATE)
    }

    factory<UserSharedPrefsRepository> {
        UserSharedPrefsRepositoryImpl(get())
    }
}