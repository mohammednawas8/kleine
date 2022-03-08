package com.example.kleine.adapters.viewpager

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.kleine.databinding.FragmentContainerBinding
import com.example.kleine.model.Category
import com.example.kleine.model.Product
import com.example.kleine.viewmodel.shopping.ShoppingViewModel

class HomeViewPager2Adapter(
    private val viewModel: ShoppingViewModel
) : RecyclerView.Adapter<HomeViewPager2Adapter.CategoryViewHolder>() {

    private val diffCallBack = object : DiffUtil.ItemCallback<Category>(){
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,diffCallBack)


    inner class CategoryViewHolder(binding: FragmentContainerBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            FragmentContainerBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            )
        )
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        Log.d("Test",position.toString())


    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}