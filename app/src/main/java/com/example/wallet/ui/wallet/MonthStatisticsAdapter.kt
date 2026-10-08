package com.example.wallet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wallet.R

class MonthStatisticsAdapter(
    private var items: List<MonthStatistics> = emptyList()
) : RecyclerView.Adapter<MonthStatisticsAdapter.ViewHolder>() {

    fun submitList(newList: List<MonthStatistics>) {
        items = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_month_statistics, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvMonthHeader: TextView = itemView.findViewById(R.id.tvMonthHeader)
        private val containerCategories: LinearLayout = itemView.findViewById(R.id.containerCategories)

        fun bind(item: MonthStatistics) {
            tvMonthHeader.text = "${item.monthYearTitle} потрачено:\n${item.totalSpent.toInt()} ₽"

            containerCategories.removeAllViews()

            for (category in item.categories) {
                val categoryTextView = TextView(itemView.context).apply {
                    text = "${category.icon} ${category.categoryName}: ${category.totalAmount.toInt()} ₽"
                    textSize = 16f

                            setPadding(0, 8, 0, 8)
                    setTextColor(context.getColor(android.R.color.tab_indicator_text))
                }
                containerCategories.addView(categoryTextView)
            }
        }
    }
}