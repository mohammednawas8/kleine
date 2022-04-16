package com.example.kleine.fragments.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.kleine.R
import com.example.kleine.databinding.FragmentHelpBinding
import com.example.kleine.databinding.FragmentProfileBinding

class HelpFragment : Fragment(R.layout.fragment_help) {
    private lateinit var binding:FragmentHelpBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHelpBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imgEmail.setOnClickListener { openEmailApp() }
        binding.imgPhone.setOnClickListener { openCallApp() }
        binding.imgCloseHelp.setOnClickListener { findNavController().navigateUp() }
    }

    private fun openCallApp() {
        val callIntent: Intent = Uri.parse("tel:123123123123").let { number ->
            Intent(Intent.ACTION_DIAL, number)
        }.also { startActivity(it) }
    }

    private fun openEmailApp() {
        Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_EMAIL, arrayOf("kleine@gmail.com")) // recipients
            putExtra(Intent.EXTRA_SUBJECT, "")
            putExtra(Intent.EXTRA_TEXT, "")
            putExtra(Intent.EXTRA_STREAM, Uri.parse("content://path/to/email/attachment"))
        }.also { startActivity(it) }
    }


}