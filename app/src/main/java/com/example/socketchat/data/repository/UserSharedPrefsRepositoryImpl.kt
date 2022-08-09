package com.example.socketchat.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.socketchat.domain.UserSharedPrefsRepository
import com.example.socketchat.utils.*

class UserSharedPrefsRepositoryImpl(
    private val userSharedPrefs: SharedPreferences
) : UserSharedPrefsRepository {

    override fun putUsername(username: String) {
        userSharedPrefs.edit {
            putString(USERNAME, username)
        }
    }

    override fun getUsername(): String {
        return userSharedPrefs.getString(USERNAME, UNDEFINED_USERNAME) ?: UNDEFINED_USERNAME
    }

    override fun isUserAuthorized(): Boolean {
        return userSharedPrefs.getString(USERNAME, UNDEFINED_USERNAME) != UNDEFINED_USERNAME
    }
}