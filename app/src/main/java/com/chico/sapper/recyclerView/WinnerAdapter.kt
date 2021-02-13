package com.chico.sapper.recyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.chico.sapper.R
import com.chico.sapper.database.entity.Winner
import com.chico.sapper.utils.ParseTime

class WinnerAdapter(private val winnerList:List<Winner>) : RecyclerView.Adapter<WinnerAdapter.WinnerViewHolder>() {
val parseTime = ParseTime()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WinnerViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.recyclerview_item,
                parent,
                false
            )
        return WinnerViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WinnerViewHolder, position: Int) {
        val currentItem = winnerList[position]

        holder.winnerNumberText.text = position.plus(1).toString()
        holder.winnerNameText.text = currentItem.name

        val pTime = parseTime.parseLongToTime(currentItem.time)
        holder.winnerTimeText.text = pTime

    }

    override fun getItemCount()= winnerList.size

    class WinnerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val winnerNumberText:TextView = itemView.findViewById(R.id.numberOfItem_cardView)
        val winnerNameText: TextView = itemView.findViewById(R.id.nameWinner_cardView)
        val winnerTimeText: TextView = itemView.findViewById(R.id.timeWinner_cardView)
    }
}