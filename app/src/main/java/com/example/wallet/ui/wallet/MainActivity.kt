package com.example.wallet

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wallet.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var spendingLimit = 30000.0
    private var currentMonthIndex = 1 // Сентябрь 2026

    private val monthsData = mutableListOf(
        MonthData(
            monthName = "Август 2026",
            income = 60000.0,
            expense = 28000.0,
            limit = 30000.0,
            transactions = listOf(
                Transaction("🎮", "Развлечения", "31.08.2026", 1200.0, false),
                Transaction("🛒", "Продукты", "25.08.2026", 4500.0, false),
                Transaction("💼", "Зарплата", "10.08.2026", 60000.0, true)
            )
        ),
        MonthData(
            monthName = "Сентябрь 2026",
            income = 65000.0,
            expense = 40500.0,
            limit = 30000.0,
            transactions = listOf(
                Transaction("🍔", "Еда", "Сегодня, 14:32", 500.0, false),
                Transaction("🚌", "Транспорт", "Сегодня, 09:15", 60.0, false),
                Transaction("💼", "Зарплата", "Вчера, 10:00", 65000.0, true),
                Transaction("🏠", "Жильё", "01.09.2026", 15000.0, false)
            )
        ),
        MonthData(
            monthName = "Октябрь 2026",
            income = 70000.0,
            expense = 12000.0,
            limit = 30000.0,
            transactions = listOf(
                Transaction("☕", "Кофе", "02.10.2026", 250.0, false),
                Transaction("💼", "Премия", "01.10.2026", 70000.0, true)
            )
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvTransactions.layoutManager = LinearLayoutManager(this)

        renderCurrentMonth()
        setupClickListeners()
        setupAddTransactionLogic()
        setupNavigation()
    }

    private fun renderCurrentMonth() {
        val data = monthsData[currentMonthIndex]

        binding.tvCurrentMonth.text = data.monthName

        val balance = data.income - data.expense
        binding.tvBalanceAmount.text = "${balance.toInt()} ₽"
        binding.tvIncome.text = "+ ${data.income.toInt()} ₽"
        binding.tvExpenses.text = "- ${data.expense.toInt()} ₽"

        if (data.expense > spendingLimit) {
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

        binding.rvTransactions.adapter = TransactionAdapter(data.transactions) { transaction ->
            Toast.makeText(
                this,
                "${transaction.title}: ${transaction.amount.toInt()} ₽",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun setupClickListeners() {
        binding.btnPrevMonth.setOnClickListener {
            if (currentMonthIndex > 0) {
                currentMonthIndex--
            } else {
                currentMonthIndex = monthsData.size - 1
            }
            renderCurrentMonth()
        }

        binding.btnNextMonth.setOnClickListener {
            if (currentMonthIndex < monthsData.size - 1) {
                currentMonthIndex++
            } else {
                currentMonthIndex = 0
            }
            renderCurrentMonth()
        }

        binding.btnSettings.setOnClickListener {
            showSettingsDialog()
        }

        binding.btnAllTransactions.setOnClickListener {
            Toast.makeText(this, "Все операции за ${monthsData[currentMonthIndex].monthName}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showSettingsDialog() {
        val input = EditText(this).apply {
            setText(spendingLimit.toInt().toString())
            inputType = android.text.InputType.TYPE_CLASS_NUMBER
        }

        AlertDialog.Builder(this)
            .setTitle("Настройки лимита")
            .setMessage("Укажите новый лимит расходов на месяц (₽):")
            .setView(input)
            .setPositiveButton("Сохранить") { _, _ ->
                val newLimit = input.text.toString().toDoubleOrNull()
                if (newLimit != null && newLimit > 0) {
                    spendingLimit = newLimit
                    renderCurrentMonth()
                    Toast.makeText(this, "Лимит обновлен: ${spendingLimit.toInt()} ₽", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Введите корректную сумму", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun setupNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            val addLayout = findViewById<View>(R.id.layoutAdd)
            val statsLayout = findViewById<View>(R.id.layoutStats)

            binding.layoutHomeContainer.visibility = View.GONE
            addLayout?.visibility = View.GONE
            statsLayout?.visibility = View.GONE

            when (item.itemId) {
                R.id.nav_home -> {
                    binding.layoutHomeContainer.visibility = View.VISIBLE
                    true
                }
                R.id.nav_add -> {
                    addLayout?.visibility = View.VISIBLE
                    true
                }
                R.id.nav_stats -> {
                    statsLayout?.visibility = View.VISIBLE
                    updateStatistics()
                    true
                }
                else -> false
            }
        }
    }

    private fun setupAddTransactionLogic() {
        val addLayout = findViewById<View>(R.id.layoutAdd) ?: return

        val etTitle = addLayout.findViewById<EditText>(R.id.etTitle)
        val etAmount = addLayout.findViewById<EditText>(R.id.etAmount)
        val rbIncome = addLayout.findViewById<RadioButton>(R.id.rbIncome)
        val btnAddSave = addLayout.findViewById<Button>(R.id.btnAddSave)

        btnAddSave?.setOnClickListener {
            val title = etTitle?.text.toString().trim()
            val amount = etAmount?.text.toString().toDoubleOrNull()
            val isIncome = rbIncome?.isChecked ?: false

            if (!title.isNullOrEmpty() && amount != null && amount > 0) {
                val icon = if (isIncome) "💰" else "💸"
                val newTransaction = Transaction(icon, title, "Сегодня", amount, isIncome)

                val currentData = monthsData[currentMonthIndex]
                val updatedTransactions = currentData.transactions.toMutableList().apply {
                    add(0, newTransaction)
                }

                val newIncome = if (isIncome) currentData.income + amount else currentData.income
                val newExpense = if (!isIncome) currentData.expense + amount else currentData.expense

                monthsData[currentMonthIndex] = currentData.copy(
                    income = newIncome,
                    expense = newExpense,
                    transactions = updatedTransactions
                )

                etTitle?.text?.clear()
                etAmount?.text?.clear()

                renderCurrentMonth()
                binding.bottomNavigation.selectedItemId = R.id.nav_home
                Toast.makeText(this, "Операция добавлена!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Заполните все поля корректно", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateStatistics() {
        val statsLayout = findViewById<View>(R.id.layoutStats) ?: return

        val tvStatsSummary = statsLayout.findViewById<TextView>(R.id.tvStatsSummary)
        val tvStatsCategories = statsLayout.findViewById<TextView>(R.id.tvStatsCategories)

        val currentData = monthsData[currentMonthIndex]
        tvStatsSummary?.text = "За ${currentData.monthName} потрачено: ${currentData.expense.toInt()} ₽"

        val categoriesText = currentData.transactions
            .filter { !it.isIncome }
            .joinToString("\n") { "${it.icon} ${it.title}: ${it.amount.toInt()} ₽" }

        tvStatsCategories?.text = categoriesText.ifEmpty { "Расходов пока нет" }
    }
}