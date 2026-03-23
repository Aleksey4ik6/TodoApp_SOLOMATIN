package com.example.todoapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.graphics.Color

class AddTaskActivity : AppCompatActivity() {

    private var selectedDiff = "Средняя"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        val etTitle = findViewById<EditText>(R.id.etTitle)
        val etDesc = findViewById<EditText>(R.id.etDesc)
        val btnEasy = findViewById<Button>(R.id.btnEasy)
        val btnMedium = findViewById<Button>(R.id.btnMedium)
        val btnHard = findViewById<Button>(R.id.btnHard)
        val btnSave = findViewById<Button>(R.id.btnSave)

        // Функция подсветки кнопок
        fun select(diff: String) {
            selectedDiff = diff
            // Сбрасываем прозрачность у всех
            btnEasy.alpha = 0.4f
            btnMedium.alpha = 0.4f
            btnHard.alpha = 0.4f
            // Подсвечиваем выбранную
            when(diff) {
                "Легкая" -> btnEasy.alpha = 1f
                "Средняя" -> btnMedium.alpha = 1f
                "Сложная" -> btnHard.alpha = 1f
            }
        }

        btnEasy.setOnClickListener { select("Легкая") }
        btnMedium.setOnClickListener { select("Средняя") }
        btnHard.setOnClickListener { select("Сложная") }

        select("Средняя") // По умолчанию

        btnSave.setOnClickListener {
            if (etTitle.text.isNotBlank()) {
                val res = Intent()
                res.putExtra("task_title", etTitle.text.toString())
                res.putExtra("task_desc", etDesc.text.toString())
                res.putExtra("task_diff", selectedDiff)
                setResult(RESULT_OK, res)
                finish()
            } else {
                etTitle.error = "Введите название"
            }
        }

        findViewById<Button>(R.id.btnCancel).setOnClickListener { finish() }
    }
}