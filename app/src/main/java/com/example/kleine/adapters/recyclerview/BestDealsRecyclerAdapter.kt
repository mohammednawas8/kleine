package com.example.kleine.adapters.recyclerview

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kleine.databinding.BestDealItemBinding
import com.example.kleine.model.Product
import com.example.kleine.util.Constants.Companion.IMAGES

class BestDealsRecyclerAdapter :
    RecyclerView.Adapter<BestDealsRecyclerAdapter.BestDealsRecyclerAdapterViewHolder>() {

    inner class BestDealsRecyclerAdapterViewHolder(val binding: BestDealItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val diffCallBack = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallBack)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BestDealsRecyclerAdapterViewHolder {
        return BestDealsRecyclerAdapterViewHolder(
            BestDealItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: BestDealsRecyclerAdapterViewHolder, position: Int) {
        val product = differ.currentList[position]
        val image = (product.images?.get(IMAGES) as List<String>)[0]
        holder.binding.apply {
            Glide.with(holder.itemView).load(image).into(imgBestDeal)
            tvDealProductName.text = product.title
            tvNewPrice.text = "$${product.newPrice}"
            tvOldPrice.text = "$${product.price}"
            tvOldPrice.paintFlags = tvOldPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }

        holder.binding.btnSeeProduct.setOnClickListener {
            onItemClick?.invoke(product)
        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    var onItemClick: ((Product) -> Unit)? = null


}