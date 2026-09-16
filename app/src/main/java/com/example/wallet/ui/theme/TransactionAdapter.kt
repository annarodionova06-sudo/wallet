package com.example.wallet

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

data class Transaction(
    val icon: String,
    val category: String,
    val date: String,
    val amount: Double,
    val isIncome: Boolean
)

class TransactionAdapter(
    private val transactions: List<Transaction>
) : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    class ViewHolder(view: android.view.View) : RecyclerView.ViewHolder(view) {
        val tvIcon: android.widget.TextView = view.findViewById(R.id.tvCategoryIcon)
        val tvCategory: android.widget.TextView = view.findViewById(R.id.tvCategoryName)
        val tvDate: android.widget.TextView = view.findViewById(R.id.tvDate)
        val tvAmount: android.widget.TextView = view.findViewById(R.id.tvAmount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = transactions[position]

        holder.tvIcon.text = item.icon
        holder.tvCategory.text = item.category
        holder.tvDate.text = item.date

        if (item.isIncome) {
            holder.tvAmount.text = "+ ${item.amount.toInt()} ₽"
            holder.tvAmount.setTextColor(Color.parseColor("#10B981"))
        } else {
            holder.tvAmount.text = "- ${item.amount.toInt()} ₽"
            holder.tvAmount.setTextColor(Color.parseColor("#EF4444"))
        }
    }

    override fun getItemCount(): Int = transactions.size
}