package com.example.calculator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import net.objecthunter.exp4j.ExpressionBuilder
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private lateinit var txtDisplay: TextView
    private var input: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txtDisplay = findViewById(R.id.txtDisplay)

        val buttons = listOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9,
            R.id.btnPlus, R.id.btnMinus, R.id.btnMultiply, R.id.btnDivide,
            R.id.btnDot
        )

        // Thiết lập sự kiện cho các nút số và toán tử
        buttons.forEach { id ->
            findViewById<Button>(id).setOnClickListener { appendToInput((it as Button).text.toString()) }
        }

        // Tính toán khi nhấn "="
        findViewById<Button>(R.id.btnEqual).setOnClickListener { calculateResult() }

        // Xóa đầu vào khi nhấn "C"
        findViewById<Button>(R.id.btnClear).setOnClickListener { clearInput() }

        // Xóa ký tự cuối cùng khi nhấn "Backspace"
        findViewById<Button>(R.id.btnBackspace).setOnClickListener { backspace() }

        // Xóa số cuối cùng khi nhấn "CE"
        findViewById<Button>(R.id.btnCE).setOnClickListener { clearLastNumber() }
    }

    // Hàm thêm số hoặc toán tử vào phép tính hiện tại
    private fun appendToInput(value: String) {
        input += value
        txtDisplay.text = input // Cập nhật TextView
    }

    // Hàm tính kết quả
    private fun calculateResult() {
        try {
            // Thay dấu "×" thành "*" để xử lý phép nhân
            val formattedInput = input.replace("×", "*")

            val expression = ExpressionBuilder(formattedInput).build()
            val result = expression.evaluate()

            // Kiểm tra nếu kết quả là số nguyên
            val displayResult = if (result % 1 == 0.0) {
                result.toLong().toString() // Hiển thị số nguyên
            } else {
                result.toString() // Hiển thị số thực cho phép chia
            }

            // Hiển thị phép tính ở dòng 1 và kết quả ở dòng 2
            txtDisplay.text = "$input\n= $displayResult"

            // Lưu kết quả để có thể tiếp tục tính toán nếu cần
            input = displayResult
        } catch (e: Exception) {
            txtDisplay.text = "Error"
            input = ""
        }
    }

    // Hàm xóa đầu vào
    private fun clearInput() {
        input = ""
        txtDisplay.text = "0"
    }

    // Hàm xóa ký tự cuối cùng
    private fun backspace() {
        if (input.isNotEmpty()) {
            input = input.dropLast(1)
            txtDisplay.text = if (input.isEmpty()) "0" else input
        }
    }

    // Hàm xóa số cuối cùng khi không có toán tử
    private fun clearLastNumber() {
        val lastOperatorIndex = input.lastIndexOfAny(charArrayOf('+', '-', '×', '/'))

        if (lastOperatorIndex != -1) {
            input = input.substring(0, lastOperatorIndex + 1) // Giữ lại phép toán
        } else {
            input = "" // Nếu không có toán tử, xóa toàn bộ
        }

        txtDisplay.text = if (input.isEmpty()) "0" else input
    }
}
