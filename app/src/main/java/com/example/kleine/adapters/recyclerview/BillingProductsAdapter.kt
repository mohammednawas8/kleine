package com.example.kleine.adapters.recyclerview

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kleine.R
import com.example.kleine.databinding.CartItemBinding
import com.example.kleine.model.CartProduct

class BillingProductsAdapter(): RecyclerView.Adapter<BillingProductsAdapter.BillingProductsAdapterViewHolder>() {

    var onPlusClick: ((CartProduct) -> Unit)? = null
    var onMinusesClick: ((CartProduct) -> Unit)? = null
    var onItemClick: ((CartProduct) -> Unit)? = null

    inner class BillingProductsAdapterViewHolder(val binding: CartItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val diffCallBack = object : DiffUtil.ItemCallback<CartProduct>() {
        override fun areItemsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem.id == newItem.id && oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallBack)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BillingProductsAdapterViewHolder {
        return BillingProductsAdapterViewHolder(
            CartItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    @SuppressLint("ResourceAsColor", "SetTextI18n")
    override fun onBindViewHolder(holder: BillingProductsAdapterViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.binding.apply {
            imgPlus.visibility = View.GONE
            imgMinus.visibility = View.GONE
            imgColor.visibility = View.GONE
            imgSize.visibility = View.GONE
            line.visibility = View.GONE
            tvQuantity.visibility = View.GONE
//            cardView.setCardBackgroundColor(R.color.g_white)
            imgCartProduct.scaleType = ImageView.ScaleType.FIT_CENTER
            Glide.with(holder.itemView).load(product.image).into(imgCartProduct)
            tvCartProductName.text = product.name
            tvProductCartPrice.text = "$ ${product.price}"

            if (product.newPrice != null && product.newPrice.isNotEmpty()) {
                tvProductCartPrice.text = "$${product.newPrice}"
            } else
                tvProductCartPrice.text = "$${product.price}"
        }


    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}
