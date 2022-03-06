package com.example.kleine.fragments.applunch

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import br.com.simplepass.loadingbutton.customViews.CircularProgressButton
import com.example.kleine.activities.LunchActivity
import com.example.kleine.R
import com.example.kleine.activities.ShoppingActivity
import com.example.kleine.databinding.FragmentLoginBinding
import com.example.kleine.resource.Resource
import com.example.kleine.viewmodel.lunchapp.KleineViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar


class LoginFragment : Fragment() {
    val TAG: String = "LoginFragment"
    val GOOGLE_REQ_CODE = 13
    private lateinit var binding: FragmentLoginBinding
    private lateinit var btnLogin: CircularProgressButton
    private lateinit var viewModel: KleineViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = (activity as LunchActivity).viewModel

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnLogin = view.findViewById(R.id.btn_login_fragment)

        onLoginClick()
        observerLogin()
        observerLoginError()
        onDontHaveAccountClick()
        onForgotPasswordClick()
        observeResetPassword()
        binding.btnFacebook.setOnClickListener {
            Snackbar.make(requireView(),resources.getText(R.string.g_coming_soon),Snackbar.LENGTH_SHORT).show()
        }

        onFacebookSignIn()
        observeSaveUserInformation()
        binding.btnLoginFragment.apply {
            btnLogin.spinningBarColor = resources.getColor(R.color.white)
            btnLogin.spinningBarWidth = resources.getDimension(R.dimen._3sdp)
        }
    }

    private fun onFacebookSignIn() {

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        binding.btnGoogle.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, GOOGLE_REQ_CODE)
        }
    }

    private fun observeResetPassword() {
        viewModel.resetPassword.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Loading -> {

                    return@Observer
                }

                is Resource.Success -> {
                    showSnackBar()
                    viewModel.resetPassword.postValue(null)
                    return@Observer
                }

                is Resource.Error -> {
                    Toast.makeText(
                        activity,
                        resources.getText(R.string.error_occurred),
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e(TAG, response.message.toString())

                    return@Observer
                }
            }
        })
    }

    private fun showSnackBar() {
        Snackbar.make(requireView(),resources.getText(R.string.g_password_reset),Snackbar.LENGTH_LONG).show()
    }

    private fun onForgotPasswordClick() {
        binding.tvForgotPassword.setOnClickListener {
            setupBottomSheetDialog()
        }
    }

    private fun setupBottomSheetDialog() {
        val dialog = BottomSheetDialog(requireContext(), R.style.DialogStyle)
        val view = layoutInflater.inflate(R.layout.forgot_password_dialog, null)
        dialog.setContentView(view)
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialog.show()

        val edEmail = view.findViewById<EditText>(R.id.edEmail)
        val btnSend = view.findViewById<Button>(R.id.btn_send)
        val btnCancel = view.findViewById<Button>(R.id.btn_cancel)

        btnSend.setOnClickListener {
            val email = edEmail.text.toString().trim()
            if (email.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                    .matches()
            ) {
                viewModel.resetPassword(email)
                dialog.dismiss()
            } else {
                edEmail.requestFocus()
                edEmail.error = resources.getText(R.string.g_check_your_email)
            }
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun onDontHaveAccountClick() {
        binding.tvDontHaveAnAccount.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun observerLoginError() {
        viewModel.loginError.observe(viewLifecycleOwner, Observer { error ->
            Log.e(TAG, error)
            Toast.makeText(activity, "Please check your information", Toast.LENGTH_LONG).show()
            btnLogin.revertAnimation()

        })


    }

    private fun observerLogin() {
        viewModel.login.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                btnLogin.revertAnimation()
                val intent = Intent(activity, ShoppingActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        })
    }

    private fun onLoginClick() {
        btnLogin.setOnClickListener {
            btnLogin.spinningBarColor = resources.getColor(R.color.white)
            btnLogin.spinningBarWidth = resources.getDimension(R.dimen._3sdp)

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
        val email = binding.edEmailLogin.text.toString().trim()

        if (email.isEmpty()) {
            binding.edEmailLogin.apply {
                error = resources.getString(R.string.email_cant_be_empty)
                requestFocus()
            }
            return null
        }


        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.edEmailLogin.apply {
                error = resources.getString(R.string.valid_email)
                requestFocus()
            }
            return null
        }


        return email

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == GOOGLE_REQ_CODE) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d("test,",account.email.toString())
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                viewModel.signInWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.e(TAG, "Google sign in failed", e)
            }
        }


    }

    private fun observeSaveUserInformation(){
        viewModel.saveUserInformationGoogleSignIn.observe(viewLifecycleOwner, Observer { response->
            when(response){
                is Resource.Loading -> {
                    Log.d(TAG,"GoogleSignIn:Loading")
                    binding.btnLoginFragment.startAnimation()
                    return@Observer
                }

                is Resource.Success -> {
                    Log.d(TAG,"GoogleSignIn:Successful")
                    binding.btnLoginFragment.stopAnimation()
                    val intent = Intent(activity,ShoppingActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    return@Observer
                }

                is Resource.Error ->{
                    Log.e(TAG,"GoogleSignIn:Error ${response.message.toString()}")
                    Toast.makeText(activity, resources.getText(R.string.error_occurred), Toast.LENGTH_LONG).show()
                    return@Observer
                }
            }

        })
    }



}