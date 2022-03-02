package com.example.kleine.fragments.settings

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.kleine.R
import com.example.kleine.databinding.FragmentEditUserInformationBinding
import com.example.kleine.model.User
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth


class EditUserInformation : Fragment() {
    private val IMAGE_REQUEST_CODE = 1
    private lateinit var binding:FragmentEditUserInformationBinding
    private val args by navArgs<EditUserInformationArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

    }

    private fun onEditImageClick() {
        binding.imgEdit.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT

            startActivityForResult(Intent.createChooser(intent,resources.getText(R.string.select_profile_image)),IMAGE_REQUEST_CODE)
        }
    }

    private fun onSaveClick() {
        binding.btnSaveProfile.setOnClickListener {

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

}