package com.example.kleine.fragments.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kleine.R
import com.example.kleine.adapters.recyclerview.ProductsRecyclerAdapter
import com.example.kleine.databinding.FragmentCupboardBinding
import com.example.kleine.firebaseDatabase.FirebaseDb
import com.example.kleine.viewmodel.shopping.ShoppingViewModel
import com.example.kleine.viewmodel.shopping.ShoppingViewModelProviderFactory

class CupboardFragment : Fragment(R.layout.fragment_cupboard) {
    private lateinit var viewModel: ShoppingViewModel
    private lateinit var binding: FragmentCupboardBinding
    private lateinit var mostOrderedCupboardsAdapter: ProductsRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val database = FirebaseDb()
        val viewModelFactory = ShoppingViewModelProviderFactory(database)
        mostOrderedCupboardsAdapter = ProductsRecyclerAdapter()
        viewModel = ViewModelProvider(this, viewModelFactory)[ShoppingViewModel::class.java]
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

        setupMostOrderedCupboard()
        observeMostOrderedCupboard()
    }

    private fun observeMostOrderedCupboard() {
        viewModel.mostCupboardOrdered.observe(viewLifecycleOwner, Observer { cupboarList ->
            mostOrderedCupboardsAdapter.differ.submitList(cupboarList.toList())
            Toast.makeText(activity, "Got them", Toast.LENGTH_SHORT).show()
        })
    }

    private fun setupMostOrderedCupboard() {
        binding.rvCupboardMostOrdered.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = mostOrderedCupboardsAdapter
        }
    }

}