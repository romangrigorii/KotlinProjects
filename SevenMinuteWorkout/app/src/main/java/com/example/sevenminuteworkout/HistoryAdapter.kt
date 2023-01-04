package com.example.sevenminuteworkout

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.sevenminuteworkout.databinding.ItemHistoryRowBinding
import com.example.sevenminuteworkout.databinding.RecyclerviewItemBinding

class HistoryAdapter(private val items : ArrayList<String>) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    // item history row binding refers to the .xml file of the row
    class ViewHolder(binding: ItemHistoryRowBinding) : RecyclerView.ViewHolder(binding.root){
        val llHistoryItemMain = binding.llHistoryItemMain
        val tvDate = binding.tvDate
        val tvNumber = binding.tvNumber
    }

    // ItemHistoryRowBinding refers to the .xml
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder { // when we create a view holder we get the inflated binding back
        return ViewHolder(ItemHistoryRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)) // this is where we specify what will inflate our single item, which should be the view
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val date : String = items[position]
        holder.tvNumber.text = (position + 1).toString()
        holder.tvDate.text = date
        if (position %2 == 0){
            holder.llHistoryItemMain.setBackgroundColor(Color.LTGRAY)
        } else {
            holder.llHistoryItemMain.setBackgroundColor(Color.WHITE)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}