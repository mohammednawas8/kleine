package com.example.kleine.adapters.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kleine.databinding.ProductItemBinding
import com.example.kleine.model.Product
import com.example.kleine.util.Constants.Companion.IMAGES

class ProductsRecyclerAdapter() :
    RecyclerView.Adapter<ProductsRecyclerAdapter.BestProductsRecyclerAdapterViewHolder>() {
    var onItemClick : ((Product) ->Unit)?=null

    inner class BestProductsRecyclerAdapterViewHolder(val binding: ProductItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id

        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,diffCallback)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BestProductsRecyclerAdapterViewHolder {
        return BestProductsRecyclerAdapterViewHolder(
            ProductItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: BestProductsRecyclerAdapterViewHolder, position: Int) {
        val product = differ.currentList[position]
        val image = (product.images?.get(IMAGES) as List<String>)[0]
        holder.binding.apply {
            Glide.with(holder.itemView).load(image).into(imgProduct)
            tvName.text = product.title
            tvPrice.text = "$${product.price}"
        }

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(differ.currentList[position])
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


}