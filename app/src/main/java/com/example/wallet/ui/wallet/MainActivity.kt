package com.example.wallet

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wallet.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Тестовые данные
        val income = 65000.0
        val expense = 40500.0
        val limit = 30000.0

        updateBalanceCard(income, expense, limit)
        setupTransactionsList()
        setupNavigation()
    }

    private fun updateBalanceCard(income: Double, expense: Double, limit: Double) {
        val balance = income - expense

        binding.tvBalanceAmount.text = "${balance.toInt()} ₽"
        binding.tvIncome.text = "+ ${income.toInt()} ₽"
        binding.tvExpenses.text = "- ${expense.toInt()} ₽"

        // Фишка "Грустного кошелька"
        if (expense > limit) {
            binding.cardBalance.setBackgroundColor(
                ContextCompat.getColor(this, R.color.limit_exceeded_bg)
            )
            binding.layoutLimitWarning.visibility = View.VISIBLE
            binding.tvMascot.text = "😭👛"
        } else {
            binding.cardBalance.setBackgroundColor(
                ContextCompat.getColor(this, R.color.blue_card_bg)
            )
            binding.layoutLimitWarning.visibility = View.GONE
            binding.tvMascot.text = "👛"
        }
    }

    private fun setupTransactionsList() {
        val transactions = listOf(
            Transaction("🍔", "Еда", "Сегодня, 14:32", 500.0, false),
            Transaction("🚌", "Транспорт", "Сегодня, 09:15", 60.0, false),
            Transaction("💼", "Зарплата", "Вчера, 10:00", 65000.0, true),
            Transaction("🏠", "Жильё", "01.09.2026", 15000.0, false),
            Transaction("🎮", "Развлечения", "31.08.2026", 1200.0, false)
        )

        binding.rvTransactions.layoutManager = LinearLayoutManager(this)
        binding.rvTransactions.adapter = TransactionAdapter(transactions)
    }

    private fun setupNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    // Уже на главной
                    true
                }
                R.id.nav_add -> {
                    Toast.makeText(this, "Экран добавления (в разработке)", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_stats -> {
                    Toast.makeText(this, "Экран статистики (в разработке)", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
    }
}