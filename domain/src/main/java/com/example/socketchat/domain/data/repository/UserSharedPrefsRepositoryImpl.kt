package com.example.socketchat.domain.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.socketchat.domain.domain.UserSharedPrefsRepository
import com.example.socketchat.domain.utils.UNDEFINED_USERNAME
import com.example.socketchat.domain.utils.USERNAME

internal class UserSharedPrefsRepositoryImpl(
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
}