package com.example.wallet

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wallet.R

class TransactionAdapter(
    private val items: List<Transaction>
) : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvIcon: TextView = view.findViewById(R.id.tvIcon)
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        val tvDate: TextView = view.findViewById(R.id.tvDate)
        val tvAmount: TextView = view.findViewById(R.id.tvAmount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.tvIcon.text = item.icon
        holder.tvTitle.text = item.title
        holder.tvDate.text = item.date

        if (item.isIncome) {
            holder.tvAmount.text = "+ ${item.amount.toInt()} ₽"
            holder.tvAmount.setTextColor(Color.parseColor("#10B981"))
        } else {
            holder.tvAmount.text = "- ${item.amount.toInt()} ₽"
            holder.tvAmount.setTextColor(Color.parseColor("#EF4444"))
        }
    }

    override fun getItemCount(): Int = items.size
}