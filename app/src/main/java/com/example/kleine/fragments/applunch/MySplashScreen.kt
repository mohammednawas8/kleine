package com.example.kleine.fragments.applunch

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.kleine.R
import com.example.kleine.activities.LunchActivity
import com.example.kleine.activities.ShoppingActivity
import com.example.kleine.databinding.FragemntSplashScreenBinding

@SuppressLint("CustomSplashScreen")
class MySplashScreen : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragemntSplashScreenBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = (activity as LunchActivity).viewModel
        val isUserSignedIn = viewModel.isUserSignedIn()
        if (isUserSignedIn) {
            val intent = Intent(requireActivity(), ShoppingActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            Handler().postDelayed({
                startActivity(intent)
            }, 1500)
        } else
            Handler().postDelayed({
                findNavController().navigate(R.id.action_mySplashScreen_to_firstScreenFragment)
            }, 1500)

    }

}