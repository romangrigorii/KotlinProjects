package com.example.lia

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.lia.databinding.ClassItemBinding

class HomeAdapter (val classList: List<Classes>,
                   private val upgradeListener: (id: Int) -> Unit,
                   private val analyzeListener: (id: Int) -> Unit,
                   private val beginListener:(id:Int)->Unit,
): RecyclerView.Adapter<HomeAdapter.HomeViewHolder>(){
    class HomeViewHolder(val itemBinding: ClassItemBinding): RecyclerView.ViewHolder(itemBinding.root){
        val clMain = itemBinding.clMain
        val ivAnalyze = itemBinding.ivAnalyze
        val ivUpgrade = itemBinding.ivUpgrade
        val tvUpgrade = itemBinding.tvUpgrade
        val bBegin = itemBinding.bBegin
        fun bindItem(cl: Classes){
            itemBinding.tvclassName.text = cl.topic.capitalize()
            itemBinding.ivLogo.setImageResource(cl.image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder(ClassItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bindItem(classList[position])
        if (position % 2 == 1){
            holder.clMain.background = getDrawable(holder.itemView.context, R.drawable.log_in_info_style_white)
        } else {
            holder.clMain.background = getDrawable(holder.itemView.context, R.drawable.log_in_info_style_grey)
        }

        holder.ivAnalyze.setOnClickListener{
            analyzeListener.invoke(classList[position].id)
        }
        holder.ivUpgrade.setOnClickListener{
            upgradeListener.invoke(classList[position].id)
        }
        holder.bBegin.setOnClickListener{
            beginListener.invoke(classList[position].id)
        }

        if (classList[position].subscription==1){
            holder.ivUpgrade.setImageResource(R.drawable.star_blue)
            holder.tvUpgrade.text = "premium"
        }
    }

    override fun getItemCount(): Int {
        return classList.size
    }

    fun getItem (id :Int): Classes {
        return classList[id]
    }

}
