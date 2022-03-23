package com.example.kleine.adapters.recyclerview

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kleine.databinding.CartItemBinding
import com.example.kleine.model.CartProduct
import com.example.kleine.util.Constants.Companion.CART_FLAG

class CartRecyclerAdapter(
    private val itemFlag: String = CART_FLAG
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
            if (product.color.isNotEmpty()) {
                val color = Color.parseColor(product.color)
                val imageDrawable = ColorDrawable(color)
                imgColor.setImageDrawable(imageDrawable)
            } else
                imgColor.visibility = View.GONE

            if (product.size.isNotEmpty())
                tvCartSize.text = product.size
            else {
                imgSize.visibility = View.GONE
                tvCartSize.visibility = View.GONE
            }

            Glide.with(holder.itemView).load(product.image).into(imgCartProduct)
            tvCartProductName.text = product.name
            tvQuantity.text = product.quantity.toString()

            if (product.newPrice != null && product.newPrice.isNotEmpty() && product.newPrice != "0") {
                tvProductCartPrice.text = "$${product.newPrice}"
            } else
                tvProductCartPrice.text = "$${product.price}"

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