package com.example.socketchat.presentation.authorization

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.socketchat.R
import com.example.socketchat.databinding.FragmentAuthorizationBinding
import com.example.socketchat.presentation.chat.ChatFragment
import com.example.socketchat.presentation.userslist.UsersListFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class AuthorizationFragment : Fragment() {

    private lateinit var binding: FragmentAuthorizationBinding

    private val viewModel by viewModel<AuthorizationViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        retainInstance = true
        binding = FragmentAuthorizationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.authorization)

        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString()
            viewModel.checkUsername(username)
        }
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.usernameError.observe(viewLifecycleOwner) {
            if (!it) {
                viewModel.sendAuth(binding.etUsername.text.toString())
            } else {
                Toast.makeText(requireContext(), "Invalid username", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.isAuthorized.observe(viewLifecycleOwner) {
            if(it) {
                binding.etUsername.isEnabled = false
                binding.btnLogin.isEnabled = false
                binding.progressBar.isVisible = true
            }
        }
        viewModel.isConnectedToServer.observe(viewLifecycleOwner) {
            if (it) {
                loadUserListFragment()
            }
        }
    }

    private fun loadUserListFragment() {
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.container, UsersListFragment())
            .commit()
    }
}