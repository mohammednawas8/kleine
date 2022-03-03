package com.example.kleine.adapters.recyclerview

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.kleine.R
import com.example.kleine.databinding.RecyclerViewAllOrdersItemBinding
import com.example.kleine.model.Order
import java.text.SimpleDateFormat

class AllOrdersAdapter : RecyclerView.Adapter<AllOrdersAdapter.AllOrdersAdapterViewHolder>() {

    inner class AllOrdersAdapterViewHolder(val binding: RecyclerViewAllOrdersItemBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<Order>(){
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem== newItem
        }
    }

    val differ = AsyncListDiffer(this,diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllOrdersAdapterViewHolder {
        return AllOrdersAdapterViewHolder(
            RecyclerViewAllOrdersItemBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            )
        )
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: AllOrdersAdapterViewHolder, position: Int) {
        val order = differ.currentList[position]
        holder.binding.apply {
            val date = SimpleDateFormat("yyyy-MM-dd").format(order.date)
            tvOrderId.text = holder.itemView.context.resources.getText(R.string.g_order).toString().plus(" #${order.id}")
            tvOrderDate.text = date
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}