package com.example.kleine.adapters.viewpager

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kleine.databinding.ChairExtraAdsBinding
import com.example.kleine.model.Product
import com.example.kleine.util.Constants.Companion.IMAGES

class AdsRecyclerAdapter : RecyclerView.Adapter<AdsRecyclerAdapter.AdsViewHolder>()
{
    val diffCallBack = object : DiffUtil.ItemCallback<Product>(){
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,diffCallBack)

    inner class AdsViewHolder(val binding:ChairExtraAdsBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdsRecyclerAdapter.AdsViewHolder {
        return AdsViewHolder(
            ChairExtraAdsBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            )
        )
    }

    override fun onBindViewHolder(holder: AdsRecyclerAdapter.AdsViewHolder, position: Int) {
        val product = differ.currentList[position]
        val images = product.images
        val image = (images!![IMAGES] as List<String>)[0]

        holder.binding.apply {
            Glide.with(holder.itemView).load(image).into(imgAd)
            tvAdPrice.text = "$${product.price}"
            tvAdProductName.text = product.title
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}