package com.example.kleine.fragments.settings

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.kleine.R
import com.example.kleine.activities.ShoppingActivity
import com.example.kleine.databinding.FragmentEditUserInformationBinding
import com.example.kleine.model.User
import com.example.kleine.resource.Resource
import com.example.kleine.viewmodel.shopping.ShoppingViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import java.io.ByteArrayOutputStream


class EditUserInformationFragment : Fragment() {
    val TAG = "EditUserInformation"
    private val IMAGE_REQUEST_CODE = 1
    private lateinit var binding: FragmentEditUserInformationBinding
    private val args by navArgs<EditUserInformationFragmentArgs>()
    private lateinit var viewModel: ShoppingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = (activity as ShoppingActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditUserInformationBinding.inflate(inflater)
        activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation)?.visibility = View.GONE
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUserInformation(args.user)
        onCloseClick()
        onSaveClick()
        onEditImageClick()
        observeUploadImage()
        onEmailClick()
        onForgotPasswordClick()
        observeResetPassword()

        observeUpdateInformation()
    }

    private fun observeResetPassword() {
        viewModel.passwordReset.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    showLoading()
                    return@observe
                }

                is Resource.Success -> {
                    hideLoading()
                    Snackbar.make(
                        requireView(),
                        resources.getText(R.string.g_password_reset).toString().plus("\n ${response.data}")
                        ,4000).show()
                    viewModel.passwordReset.postValue(null)
                    return@observe
                }

                is Resource.Error -> {
                    hideLoading()
                    Toast.makeText(
                        activity,
                        resources.getText(R.string.error_occurred),
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e(TAG, response.message.toString())
                    return@observe
                }
            }
        }    }

    private fun onForgotPasswordClick() {
        binding.tvUpdatePassword.setOnClickListener {
            setupAlertDialog()
        }
    }

    private fun setupAlertDialog() {

            val alertDialog = AlertDialog.Builder(context).create()
            val view = LayoutInflater.from(context).inflate(R.layout.delete_alert_dialog,null,false)
            alertDialog.setView(view)
            val title = view.findViewById<TextView>(R.id.tv_delete_item)
            val message = view.findViewById<TextView>(R.id.tv_delete_message)
            val btnConfirm = view.findViewById<Button>(R.id.btn_yes)
            val btnCancel = view.findViewById<Button>(R.id.btn_no)
            title.text = resources.getText(R.string.g_reset_password)
            message.text = resources.getText(R.string.g_reset_password_message).toString().plus("\n ${args.user.email}")
            btnConfirm.text = resources.getText(R.string.g_send)
            btnCancel.text = resources.getText(R.string.g_cancel)


            btnConfirm.setOnClickListener {
                viewModel.resetPassword(args.user.email.trim())
                alertDialog.dismiss()
            }

            btnCancel.setOnClickListener {
                alertDialog.dismiss()
            }

            alertDialog.show()

    }

    private fun onEmailClick() {
        binding.edEmail.setOnClickListener {
            binding.edEmail.apply {
                Snackbar.make(requireView(),resources.getText(R.string.g_cant_change_email_message),4500).show()
            }
        }
    }

    private fun observeUpdateInformation() {
        viewModel.updateUserInformation.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    showLoading()
                    return@observe
                }

                is Resource.Success -> {
                    hideLoading()
                    findNavController().navigateUp()
                    viewModel.updateUserInformation.postValue(null)
                    viewModel.uploadProfileImage.postValue(null)
                    return@observe
                }

                is Resource.Error -> {
                    hideLoading()
                    Toast.makeText(
                        activity,
                        resources.getText(R.string.error_occurred),
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e(TAG, response.message.toString())
                    return@observe
                }
            }
        }
    }

    private fun observeUploadImage() {
        viewModel.uploadProfileImage.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    showLoading()
                    return@observe
                }

                is Resource.Success -> {
                    val firstName = binding.edFirstName.text.toString()
                    val lastName = binding.edLastName.text.toString()
                    val email = binding.edEmail.text.toString()

                    viewModel.updateInformation(firstName, lastName, email, response.data!!)
                    return@observe
                }

                is Resource.Error -> {
                    hideLoading()
                    Toast.makeText(
                        activity,
                        resources.getText(R.string.error_occurred),
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e(TAG, response.message.toString())
                    return@observe
                }
            }
        }
    }


    private fun showLoading() {
        binding.apply {
            progressbarEditProfile.visibility = View.VISIBLE
            btnSaveProfile.visibility = View.INVISIBLE
        }
    }

    private fun hideLoading() {
        binding.apply {
            progressbarEditProfile.visibility = View.GONE
            btnSaveProfile.visibility = View.VISIBLE
        }
    }

    private fun onEditImageClick() {
        binding.imgEdit.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT

            startActivityForResult(
                Intent.createChooser(
                    intent,
                    resources.getText(R.string.select_profile_image)
                ), IMAGE_REQUEST_CODE
            )
        }
    }

    private fun onSaveClick() {
        binding.btnSaveProfile.setOnClickListener {
            if (isPicked)
                imageArray?.let { viewModel.uploadProfileImage(it) }
            else {
                val firstName = binding.edFirstName.text.toString()
                val lastName = binding.edLastName.text.toString()
                val email = binding.edEmail.text.toString()
                val image=""
                viewModel.updateInformation(firstName,lastName,email,image)
            }
        }

    }

    private fun onCloseClick() {
        binding.imgCloseEditProfile.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setUserInformation(user: User) {

        binding.apply {
            Glide.with(requireView()).load(user.imagePath)
                .error(R.drawable.ic_default_profile_picture).into(imgUser)

            edFirstName.setText(user.firstName)
            edLastName.setText(user.lastName)
            edEmail.setText(FirebaseAuth.getInstance().currentUser?.email)
        }
    }

    var imageArray: ByteArray? = null
    private var isPicked = false
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                val imageUri = data.data
                isPicked = true
                Glide.with(this).load(imageUri).error(R.drawable.ic_default_profile_picture)
                    .into(binding.imgUser)
                val imageByteArray: ByteArray = compressImage(imageUri)
                imageArray = imageByteArray
            }
        }
    }

    private fun compressImage(imageUri: Uri?): ByteArray {
        val imageInBitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, imageUri)
        val imageByteArray = ByteArrayOutputStream()
        imageInBitmap.compress(
            Bitmap.CompressFormat.JPEG,
            20,
            imageByteArray
        )

        return imageByteArray.toByteArray()
    }


}