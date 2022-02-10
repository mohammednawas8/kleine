package com.example.kleine.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.simplepass.loadingbutton.customViews.CircularProgressButton
import com.example.kleine.R
import com.example.kleine.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    private lateinit var binding:FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnRegister = view.findViewById<CircularProgressButton>(R.id.btn_login)

        btnRegister.setOnClickListener {
            btnRegister.spinningBarColor = resources.getColor(R.color.g_white)
            btnRegister.spinningBarWidth = resources.getDimension(R.dimen._20sdp)
            btnRegister.startAnimation()

            //btnRegister.revertAnimation()
        }

    }

}