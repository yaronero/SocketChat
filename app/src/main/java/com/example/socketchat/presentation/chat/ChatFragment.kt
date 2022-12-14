package com.example.socketchat.presentation.chat

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.example.socketchat.R
import com.example.socketchat.databinding.FragmentChatBinding
import com.example.socketchat.presentation.chat.adapter.ChatAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding

    private val anotherUserId by lazy {
        arguments?.getString(ANOTHER_USER_ID)!!
    }

    private val viewModel: ChatViewModel by viewModel {
        parametersOf(
            anotherUserId
        )
    }

    private val adapter by lazy {
        ChatAdapter(anotherUserId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        retainInstance = true
        binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = arguments?.getString(ANOTHER_USER_NAME)!!

        setupObservers()
        setupListeners()
        setupAdapter()
    }

    private fun setupObservers() {
        viewModel.newMessages.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    private fun setupListeners() {
        binding.btnSendMessage.setOnClickListener {
            val message = binding.etMessage.text.toString()
            binding.etMessage.setText("")
            viewModel.sendMessage(message)
        }
    }

    private fun setupAdapter() {
        binding.rvMessages.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    companion object {
        private const val ANOTHER_USER_ID = "another_user_id"
        private const val ANOTHER_USER_NAME = "another_user_name"

        fun newInstance(anotherUserId: String, username: String): ChatFragment {
            return ChatFragment().apply {
                arguments = bundleOf(
                    ANOTHER_USER_ID to anotherUserId,
                    ANOTHER_USER_NAME to username
                )
            }
        }
    }
}