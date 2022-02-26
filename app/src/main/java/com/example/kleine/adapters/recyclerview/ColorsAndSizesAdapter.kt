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

    var onItemClick : ((String) -> Unit)?=null
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

    private var selectedPosition = -1

    override fun onBindViewHolder(holder: ColorsAndSizesAdapterViewHolder, position: Int) {
        if (type == COLORS_TYPE) {
            val color = differ.currentList[position]
            if(position == selectedPosition) { //Select case
                holder.binding.apply {
                    val color = Color.parseColor(color)
                    val imageDrawable = ColorDrawable(color)
                    imgContent.setImageDrawable(imageDrawable)
                    imgShadow.visibility = View.INVISIBLE
                    imgDone.visibility = View.VISIBLE
                }
            }
            else{
                holder.binding.apply {
                    val color = Color.parseColor(color)
                    val imageDrawable = ColorDrawable(color)
                    imgContent.setImageDrawable(imageDrawable)
                    imgShadow.setImageDrawable(imageDrawable)
                    imgShadow.visibility = View.INVISIBLE
                    imgDone.visibility = View.GONE
                    tvSize.visibility = View.GONE
                }
            }

            holder.itemView.setOnClickListener {
                if (selectedPosition >= 0)
                    notifyItemChanged(selectedPosition)
                selectedPosition = holder.adapterPosition
                notifyItemChanged(selectedPosition)
                onItemClick!!.invoke(color)

            }
        }
        else{
            val size = differ.currentList[position] as String

            if(selectedPosition == position){
                holder.binding.apply {
                    imgShadow.visibility = View.VISIBLE
                    imgContent.visibility = View.VISIBLE
                    imgContent.setImageResource(R.color.g_icon_tint)
                    imgShadow.setImageResource(R.color.g_circle_shadow)
                    tvSize.visibility = View.VISIBLE
                    tvSize.text = size
                }

            }else{
                holder.binding.apply {
                    imgShadow.visibility = View.INVISIBLE
                    imgContent.visibility = View.VISIBLE
                    imgDone.visibility = View.INVISIBLE
                    imgContent.setImageResource(R.color.g_icon_tint)
                    imgShadow.setImageResource(R.color.g_icon_tint)
                    tvSize.visibility = View.VISIBLE
                    tvSize.text = size
                }
            }

            holder.itemView.setOnClickListener {
                if(selectedPosition >= 0)
                    notifyItemChanged(selectedPosition)
                selectedPosition = holder.adapterPosition
                notifyItemChanged(selectedPosition)

                onItemClick!!.invoke(size)
            }


        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}