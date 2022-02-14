package com.example.kleine.fragments.categories

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.kleine.R
import com.example.kleine.activities.ShoppingActivity
import com.example.kleine.firebaseDatabase.FirebaseDb
import com.example.kleine.viewmodel.shopping.ShoppingViewModel
import com.example.kleine.viewmodel.shopping.ShoppingViewModelProviderFactory

class FurnitureFragment : Fragment(R.layout.fragment_furniture) {
    private lateinit var viewModel: ShoppingViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val database = FirebaseDb()
        viewModel = (activity as ShoppingActivity).viewModel
    }

}