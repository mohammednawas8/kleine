package com.example.kleine.fragments.categories

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.kleine.R
import com.example.kleine.firebaseDatabase.FirebaseDb
import com.example.kleine.viewmodel.shopping.ShoppingViewModel
import com.example.kleine.viewmodel.shopping.ShoppingViewModelProviderFactory

class TableFragment : Fragment(R.layout.fragment_table) {
    private lateinit var viewModel: ShoppingViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val database = FirebaseDb()
        val viewModelFactory = ShoppingViewModelProviderFactory(database)
        viewModel = ViewModelProvider(this,viewModelFactory)[ShoppingViewModel::class.java]
    }

}