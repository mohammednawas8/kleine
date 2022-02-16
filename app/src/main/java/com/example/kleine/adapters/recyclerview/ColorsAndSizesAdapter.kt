package com.example.kleine.adapters.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.kleine.databinding.ColorAndSizesBinding
import com.example.kleine.util.Constants.Companion.COLORS_TYPE

class ColorsAndSizesAdapter(var type: String = COLORS_TYPE) :
    RecyclerView.Adapter<ColorsAndSizesAdapter.ColorsAndSizesAdapterViewHolder>() {

    inner class ColorsAndSizesAdapterViewHolder(val binding: ColorAndSizesBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ColorsAndSizesAdapterViewHolder {
        return ColorsAndSizesAdapterViewHolder(
            ColorAndSizesBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    private var previousSelectedItem = -1
    private var currentSlectedItem = -1

    override fun onBindViewHolder(holder: ColorsAndSizesAdapterViewHolder, position: Int) {
        if (type == COLORS_TYPE) {
            val color = differ.currentList[position]
            holder.binding.apply {
                imgContent.setBackgroundColor(color.toInt())
                imgShadow.visibility = View.INVISIBLE
                tvSize.visibility = View.GONE
            }
            holder.itemView.setOnClickListener {
                holder.binding.apply {
                    holder.binding.imgShadow.visibility = View.VISIBLE
                }
            }
        }
        else{
            val size = differ.currentList[position]
            holder.binding.apply {
                imgShadow.visibility = View.INVISIBLE
                imgContent.visibility = View.VISIBLE
                tvSize.text = size
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}