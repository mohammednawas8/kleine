package com.example.kleine.adapters.recyclerview

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kleine.databinding.CartItemBinding
import com.example.kleine.model.CartProduct
import com.example.kleine.resource.Resource
import com.example.kleine.util.Constants.Companion.CART_FLAG
import com.example.kleine.viewmodel.shopping.cart.CartViewModel

class CartRecyclerAdapter(
    val itemFlag: String = CART_FLAG
) : RecyclerView.Adapter<CartRecyclerAdapter.CartRecyclerAdapterViewHolder>() {

    var onPlusClick: ((CartProduct) -> Unit)? = null
    var onMinusesClick: ((CartProduct) -> Unit)? = null
    var onItemClick: ((CartProduct) -> Unit)? = null

    inner class CartRecyclerAdapterViewHolder(val binding: CartItemBinding) :
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
    ): CartRecyclerAdapterViewHolder {
        return CartRecyclerAdapterViewHolder(
            CartItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CartRecyclerAdapterViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.binding.apply {
            val color = Color.parseColor(product.color)
            val imageDrawable = ColorDrawable(color)
            Glide.with(holder.itemView).load(product.image).into(imgCartProduct)
            tvCartProductName.text = product.name
            tvProductCartPrice.text = "$${product.price}"
            tvQuantity.text = product.quantity.toString()
            tvCartSize.text = product.size
            imgColor.setImageDrawable(imageDrawable)

            if (itemFlag != CART_FLAG)
                holder.binding.apply {
                    imgPlus.visibility = View.INVISIBLE
                    imgMinus.visibility = View.INVISIBLE
                    tvQuantity.text = product.quantity.toString()
                }
            else {

                imgPlus.setOnClickListener {
                    onPlusClick!!.invoke(product)
                }

                imgMinus.setOnClickListener {
                    onMinusesClick!!.invoke(product)
                }


                holder.itemView.setOnClickListener {
                    onItemClick!!.invoke(product)
                }
            }
        }


    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}