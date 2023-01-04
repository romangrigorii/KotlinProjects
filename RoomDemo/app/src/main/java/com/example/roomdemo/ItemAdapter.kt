package com.example.roomdemo

import android.animation.ValueAnimator.AnimatorUpdateListener
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.roomdemo.databinding.EditLayoutBinding
import com.example.roomdemo.databinding.RecyclerviewItemBinding

// the adapted is useful for recycler view to populate the data

class ItemAdapter(private val items : ArrayList<EmployeeEntity> ,
                  private val updateListener:(id:Int)->Unit,
                  private val deleteListener:(id:Int)->Unit
    ) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    class ViewHolder(binding: RecyclerviewItemBinding) : RecyclerView.ViewHolder(binding.root){
        val llMain = binding.llMain
        val tvName = binding.tvName
        val tvEmail = binding.tvEmail
        val ivEdit = binding.ivEdit
        val ivDelete = binding.ivDelete
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder { // when we create a view holder we get the inflated binding back
        return ViewHolder(RecyclerviewItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        val item = items[position]

        holder.tvName.text = item.name
        holder.tvEmail.text = item.email

        if (position % 2 == 0){
            holder.llMain.setBackgroundColor(ContextCompat.getColor(context, R.color.lightGray))
        } else {
            holder.llMain.setBackgroundColor(ContextCompat.getColor(context, R.color.white))

        }
        holder.ivEdit.setOnClickListener{
            updateListener.invoke(item.id)
        }
        holder.ivDelete.setOnClickListener{
            deleteListener.invoke(item.id)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}