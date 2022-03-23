package com.example.kleine.adapters.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kleine.databinding.ChairExtraAdsItemBinding
import com.example.kleine.model.Product
import com.example.kleine.util.Constants.Companion.IMAGES

class AdsRecyclerAdapter : RecyclerView.Adapter<AdsRecyclerAdapter.AdsViewHolder>() {
    val diffCallBack = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallBack)

    inner class AdsViewHolder(val binding: ChairExtraAdsItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdsViewHolder {
        return AdsViewHolder(
            ChairExtraAdsItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: AdsViewHolder, position: Int) {
        val product = differ.currentList[position]
        val images = product.images
        val image = (images!![IMAGES] as List<String>)[0]

        holder.binding.apply {
            Glide.with(holder.itemView).load(image).into(imgAd)
            tvAdPrice.text = "$${product.price}"
            tvAdName.text = product.title
        }

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(product)
        }


        holder.binding.btnAddToCart.setOnClickListener {
            onAddToCartClick?.invoke(product)
        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    var onItemClick: ((Product) -> Unit)? = null

    var onAddToCartClick: ((Product) -> Unit)? = null

}