package com.example.kleine.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.simplepass.loadingbutton.customViews.CircularProgressButton
import com.example.kleine.R
import com.example.kleine.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

    private lateinit var binding : FragmentLoginBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val btnLogin = view.findViewById<CircularProgressButton>(R.id.btn_login)
        btnLogin.setOnClickListener {
            btnLogin.apply {
                spinningBarColor = resources.getColor(R.color.g_white)
                spinningBarWidth = resources.getDimension(R.dimen._20sdp)
            }

            btnLogin.startAnimation()
        }
    }



}