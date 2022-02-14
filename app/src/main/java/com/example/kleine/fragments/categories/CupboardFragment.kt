package com.example.kleine.fragments.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.kleine.R
import com.example.kleine.databinding.FragmentCupboardBinding
import com.example.kleine.firebaseDatabase.FirebaseDb
import com.example.kleine.viewmodel.shopping.ShoppingViewModel
import com.example.kleine.viewmodel.shopping.ShoppingViewModelProviderFactory

class CupboardFragment : Fragment(R.layout.fragment_cupboard) {
    private lateinit var viewModel: ShoppingViewModel
    private lateinit var binding:FragmentCupboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val database = FirebaseDb()
        val viewModelFactory = ShoppingViewModelProviderFactory(database)
        viewModel = ViewModelProvider(this,viewModelFactory)[ShoppingViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentCupboardBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

}