package com.example.socketchat.di

import android.content.Context
import com.example.socketchat.data.repository.ConnectionRepositoryImpl
import com.example.socketchat.data.repository.UserSharedPrefsRepositoryImpl
import com.example.socketchat.domain.ConnectionRepository
import com.example.socketchat.domain.UserSharedPrefsRepository
import com.example.socketchat.utils.SHARED_PREFS_USER
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