package com.example.socketchat.presentation.userslist

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.socketchat.R
import com.example.socketchat.data.dtomodels.User
import com.example.socketchat.databinding.FragmentUsersListBinding
import com.example.socketchat.presentation.authorization.AuthorizationFragment
import com.example.socketchat.presentation.chat.ChatFragment
import com.example.socketchat.presentation.userslist.adapter.UsersListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class UsersListFragment : Fragment() {

    private lateinit var binding: FragmentUsersListBinding

    private val adapter by lazy {
        UsersListAdapter(::onItemClickListener)
    }

    private val viewModel: UsersListViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.log_out -> logOut()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun logOut() {
        viewModel.logOut()
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.container, AuthorizationFragment())
            .commit()
    }

    private fun setupAdapter() {
        binding.rvUsersList.adapter = adapter
    }

    private fun onItemClickListener(user: User) {
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.container, ChatFragment.newInstance(user.id))
            .addToBackStack(null)
            .commit()
    }
}