package com.example.socketchat.presentation.userslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.socketchat.databinding.FragmentUsersListBinding

class UsersListFragment : Fragment() {

    private lateinit var binding: FragmentUsersListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        retainInstance = true
        binding = FragmentUsersListBinding.inflate(inflater, container, false)
        return binding.root
    }
}