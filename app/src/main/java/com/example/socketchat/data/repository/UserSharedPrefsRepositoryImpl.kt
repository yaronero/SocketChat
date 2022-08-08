package com.example.socketchat.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.socketchat.domain.UserSharedPrefsRepository
import com.example.socketchat.utils.IS_USER_AUTHORIZED
import com.example.socketchat.utils.UNDEFINED_ID
import com.example.socketchat.utils.USER_ID

class UserSharedPrefsRepositoryImpl(
    private val userSharedPrefs: SharedPreferences
) : UserSharedPrefsRepository {

    override fun putId(id: String) {
        userSharedPrefs.edit {
            putString(USER_ID, id)
        }
    }

    override fun getId(): String {
        return userSharedPrefs.getString(USER_ID, UNDEFINED_ID) ?: UNDEFINED_ID
    }

    override fun setIfUserAuthorized(isUserAuthorized: Boolean) {
        userSharedPrefs.edit {
            putBoolean(USER_ID, isUserAuthorized)
        }
    }

    override fun isUserAuthorized(): Boolean {
        return userSharedPrefs.getBoolean(IS_USER_AUTHORIZED, false)
    }
}