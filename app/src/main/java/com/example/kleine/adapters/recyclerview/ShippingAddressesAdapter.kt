package com.example.kleine.adapters.recyclerview

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.kleine.R
import com.example.kleine.databinding.RecyclerviewShippingItemBinding
import com.example.kleine.model.Address
import com.example.kleine.util.Constants.Companion.SELECT_ADDRESS_FLAG

class ShippingAddressesAdapter(
    val ADDRESS_CLICK_FLAG : String
) :
    RecyclerView.Adapter<ShippingAddressesAdapter.ShippingAddressesAdapterViewHolder>() {
    inner class ShippingAddressesAdapterViewHolder(val binding: RecyclerviewShippingItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<Address>() {
        override fun areItemsTheSame(oldItem: Address, newItem: Address): Boolean {
            return oldItem.addressTitle == newItem.addressTitle
        }

        override fun areContentsTheSame(oldItem: Address, newItem: Address): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ShippingAddressesAdapterViewHolder {
        return ShippingAddressesAdapterViewHolder(
            RecyclerviewShippingItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
    var selectedAddress = -1
    override fun onBindViewHolder(holder: ShippingAddressesAdapterViewHolder, position: Int) {
        val address = differ.currentList[position]

        if(ADDRESS_CLICK_FLAG == SELECT_ADDRESS_FLAG) {
            if (selectedAddress == position) {
                holder.binding.btnAddress.apply {
                    setBackgroundColor(resources.getColor(R.color.g_dark_blue))
                    text = address.addressTitle
                    setTextColor(resources.getColor(R.color.white))
                }
            } else {
                holder.binding.btnAddress.apply {
                    setBackgroundResource(R.drawable.unselected_button_background)
                    text = address.addressTitle
                    setTextColor(resources.getColor(R.color.g_icon_tint))
                }
            }

            holder.binding.btnAddress.setOnClickListener {

                if (selectedAddress >= 0)
                    notifyItemChanged(selectedAddress)
                selectedAddress = holder.adapterPosition
                notifyItemChanged(selectedAddress)
                onBtnClick?.invoke(address)
            }

        }else {
            holder.binding.btnAddress.apply {
                setBackgroundResource(R.drawable.unselected_button_background)
                text = address.addressTitle
                setTextColor(resources.getColor(R.color.g_icon_tint))
            }

            holder.binding.btnAddress.setOnClickListener {
                onBtnClick?.invoke(address)
            }
        }


    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    var onBtnClick : ((Address)->Unit)?=null
}