package com.example.kleine.fragments.applunch

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import br.com.simplepass.loadingbutton.customViews.CircularProgressButton
import com.example.kleine.activities.LunchActivity
import com.example.kleine.R
import com.example.kleine.databinding.FragmentRegisterBinding
import com.example.kleine.model.User
import com.example.kleine.resource.Resource
import com.example.kleine.viewmodel.lunchapp.KleineViewModel

private const val TAG = "RegisterFragment"

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    lateinit var viewModel: KleineViewModel
    lateinit var btnRegister: CircularProgressButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = (activity as LunchActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnRegister = view.findViewById(R.id.btn_login)

        onRegisterBtnClick()
        observeSaveUserInformation()
        onLoginClick()
    }

    private fun onLoginClick() {
        binding.tvDontHaveAnAccount.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }


    private fun onRegisterBtnClick() {
        btnRegister.setOnClickListener {
            btnRegister.spinningBarColor = resources.getColor(R.color.white)
            btnRegister.spinningBarWidth = resources.getDimension(R.dimen._3sdp)
            val user = getUser()
            val password = getPassword()
            user?.let { user ->
                password?.let { password ->
                    viewModel.registerNewUser(user, password)
                    btnRegister.startAnimation()
                }
            }
        }
    }

    private fun observeSaveUserInformation() {
        viewModel.register.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Loading -> {
                    Log.d(TAG, "EmailRegister:Loading")
                    btnRegister.startAnimation()
                    return@Observer
                }

                is Resource.Success -> {
                    Log.d(TAG, "EmailRegister:Successful")
                    btnRegister.stopAnimation()
                    Toast.makeText(
                        activity,
                        resources.getText(R.string.signed_up_successfully),
                        Toast.LENGTH_SHORT
                    ).show()
                    viewModel.logOut()
                    findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                    viewModel.register.postValue(null)
                }

                is Resource.Error -> {
                    Log.e(TAG, "EmailRegister:Error ${response.message.toString()}")
                    Toast.makeText(
                        activity,
                        resources.getText(R.string.error_occurred),
                        Toast.LENGTH_LONG
                    ).show()
                    return@Observer
                }
            }
        })
    }


    private fun getUser(): User? {
        val firstName = binding.edFirstName.text.toString().trim()
        val lastName = binding.edLastName.text.toString().trim()
        val email = binding.edEmail.text.toString().trim()

        if (firstName.isEmpty()) {
            binding.edFirstName.apply {
                error = resources.getString(R.string.first_name_cant_be_empty)
                requestFocus()
            }
            return null
        }

        if (lastName.isEmpty()) {
            binding.edLastName.apply {
                error = resources.getString(R.string.last_name_cant_be_empty)
                requestFocus()
            }
            return null
        }

        if (email.isEmpty()) {
            binding.edEmail.apply {
                error = resources.getString(R.string.email_cant_be_empty)
                requestFocus()
            }
            return null
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.edEmail.apply {
                error = resources.getString(R.string.valid_email)
                requestFocus()
            }
            return null
        }


        return User(firstName, lastName, email)
    }

    private fun getPassword(): String? {
        val password = binding.edPassword.text.toString().trim()
        if (password.isEmpty()) {
            binding.edPassword.apply {
                error = resources.getString(R.string.password_cant_be_empty)
                requestFocus()
            }
            return null
        }

        if (password.length < 6) {
            binding.edPassword.apply {
                error = resources.getString(R.string.password_at_least_six)
                requestFocus()
            }
            return null
        }
        return password
    }

}