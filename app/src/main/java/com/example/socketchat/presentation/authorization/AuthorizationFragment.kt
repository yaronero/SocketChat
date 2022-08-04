package com.example.socketchat.presentation.authorization

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.socketchat.databinding.FragmentAuthorizationBinding
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

        viewModel.connectToServer()
    }
}