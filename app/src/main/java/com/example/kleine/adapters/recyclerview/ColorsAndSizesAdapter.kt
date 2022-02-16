package com.example.kleine.adapters.recyclerview

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.kleine.R
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
                val color = Color.parseColor(color)
                val imageDrawable = ColorDrawable(color)
                imgContent.setImageDrawable(imageDrawable)
                imgShadow.setImageDrawable(imageDrawable)

                tvSize.visibility = View.GONE
            }
            holder.itemView.setOnClickListener {
                holder.binding.apply {
                    imgShadow.setImageResource(R.color.g_circle_shadow)
                }
            }
        }
        else{
            val size = differ.currentList[position] as String
            holder.binding.apply {
                imgShadow.visibility = View.VISIBLE
                imgContent.visibility = View.VISIBLE
                imgContent.setImageResource(R.color.g_icon_tint)
                imgShadow.setImageResource(R.color.g_icon_tint)
                tvSize.text = size
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}