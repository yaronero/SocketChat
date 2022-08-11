package com.example.socketchat.presentation.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.example.socketchat.data.dtomodels.MessageDto
import com.example.socketchat.data.dtomodels.wrappers.MessageWrapper
import com.example.socketchat.data.dtomodels.User
import com.example.socketchat.databinding.FragmentChatBinding
import com.example.socketchat.presentation.chat.adapter.ChatAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.util.*

class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding

    private val viewModel: ChatViewModel by viewModel {
        parametersOf(
            arguments?.getString(ANOTHER_USER_ID)!!
        )
    }

    private val adapter by lazy {
        ChatAdapter(arguments?.getString(ANOTHER_USER_ID)!!)
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

        setupObservers()
        setupListeners()
        setupAdapter()
    }

    private fun setupObservers() {
        viewModel.newMessages.observe(viewLifecycleOwner) {
            val list = adapter.currentList.plus(it)
            adapter.submitList(list)
        }
    }

    private fun setupListeners() {
        binding.btnSendMessage.setOnClickListener {
            val message = binding.etMessage.text.toString()
            if (message.isNotBlank()) {
                adapter.submitList(
                    adapter.currentList.plus(
                        MessageWrapper(
                            UUID.randomUUID().toString(),
                            MessageDto(User(viewModel.getId(), viewModel.getUsername()), message)
                        )
                    )
                )
                binding.etMessage.setText("")
                viewModel.sendMessage(message)
            }
        }
    }

    private fun setupAdapter() {
        binding.rvMessages.adapter = adapter
    }

    companion object {
        private const val ANOTHER_USER_ID = "another_user_id"

        fun newInstance(anotherUserId: String): ChatFragment {
            return ChatFragment().apply {
                arguments = bundleOf(ANOTHER_USER_ID to anotherUserId)
            }
        }
    }
}