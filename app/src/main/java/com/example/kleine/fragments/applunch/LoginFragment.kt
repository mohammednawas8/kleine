package com.example.kleine.fragments.applunch

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import br.com.simplepass.loadingbutton.customViews.CircularProgressButton
import com.example.kleine.activities.LunchActivity
import com.example.kleine.R
import com.example.kleine.activities.ShoppingActivity
import com.example.kleine.databinding.FragmentLoginBinding
import com.example.kleine.viewmodel.lunchapp.KleineViewModel


class LoginFragment : Fragment() {
     val TAG:String = "LoginFragment"

    private lateinit var binding : FragmentLoginBinding
    private lateinit var btnLogin : CircularProgressButton
    private lateinit var viewModel: KleineViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = (activity as LunchActivity).viewModel

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnLogin = view.findViewById(R.id.btn_login_fragment)

        onLoginClick()
        observerLogin()
        observerLoginError()

    }

    private fun observerLoginError() {
        viewModel.loginError.observe(viewLifecycleOwner, Observer { error->
            Log.e(TAG,error)
            Toast.makeText(activity, error, Toast.LENGTH_SHORT).show()
            btnLogin.revertAnimation()
        })
    }

    private fun observerLogin() {
        viewModel.login.observe(viewLifecycleOwner, Observer {
            if(it == true){
                btnLogin.revertAnimation()
                val intent = Intent(activity,ShoppingActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        })
    }

    private fun onLoginClick() {
        btnLogin.setOnClickListener {
            btnLogin.apply {
                spinningBarColor = resources.getColor(R.color.white)
                spinningBarWidth = resources.getDimension(R.dimen._20sdp)
            }
            val email = getEmail()?.trim()
            val password = getPassword()
            email?.let {
                password?.let {
                    btnLogin.startAnimation()
                    viewModel.loginUser(email, password)
                }
            }
        }
    }

    private fun getPassword(): String? {
        val password = binding.edPasswordLogin.text.toString()

        if (password.isEmpty()) {
            binding.edPasswordLogin.apply {
                error = resources.getString(R.string.password_cant_be_empty)
                requestFocus()
            }
            return null
        }

        if (password.length < 6) {
            binding.edPasswordLogin.apply {
                error = resources.getString(R.string.password_at_least_six)
                requestFocus()
            }
            return null
        }
        return password
    }

    private fun getEmail(): String? {
        val email = binding.edEmailLogin.text.toString()

        if (email.isEmpty()) {
            binding.edEmailLogin.apply {
                error = resources.getString(R.string.email_cant_be_empty)
                requestFocus()
            }
            return null
        }

        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.edEmailLogin.apply {
                error = resources.getString(R.string.valid_email)
                requestFocus()
            }
            return null
        }


        return email

    }

}