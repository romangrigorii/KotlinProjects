package com.example.sevenminuteworkout

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.sevenminuteworkout.databinding.ItemExerciseStatusBinding

class ExerciseStatusAdapter (val list: ArrayList<ExerciseModel>) : RecyclerView.Adapter<ExerciseStatusAdapter.ViewHolder>(){
    class ViewHolder(binding : ItemExerciseStatusBinding) : RecyclerView.ViewHolder(binding.root){
        val tvItem = binding.tvItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemExerciseStatusBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model : ExerciseModel = list[position]
        holder.tvItem.text = model.get().toString()

        when{
            model.getIsSelected() -> {
                holder.tvItem.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.item_circular_thin_color_accent_border)
                holder.tvItem.setTextColor(Color.parseColor("#212121"))
            }
            model.getIsCompleted() -> {
                holder.tvItem.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.item_circular_color_accent_background)
                holder.tvItem.setTextColor(Color.parseColor("#FFFFFF"))
            }
//            else -> {
//                holder.tvItem.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.item_circular_color_grey_background)
//                holder.tvItem.setTextColor(Color.parseColor("#212121"))
//            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}