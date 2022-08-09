package com.example.socketchat.presentation.userslist

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.socketchat.databinding.FragmentUsersListBinding
import com.example.socketchat.presentation.userslist.adapter.UsersListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class UsersListFragment : Fragment() {

    private lateinit var binding: FragmentUsersListBinding

    private val adapter by lazy {
        UsersListAdapter()
    }

    private val viewModel: UsersListViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        retainInstance = true
        binding = FragmentUsersListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getAllUsers()
        setupAdapter()
        viewModel.usersList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    private fun setupAdapter() {
        binding.rvUsersList.adapter = adapter
    }
}