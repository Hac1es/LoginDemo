package com.example.tunglete

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent

class MainActivity : AppCompatActivity() {

    // Khai báo biến
    lateinit var db: DBHelper
    lateinit var etUser: EditText
    lateinit var etPass: EditText
    lateinit var btnLogin: Button
    lateinit var btnReg: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Load file XML

        // Khởi tạo DB
        db = DBHelper(this)

        // Ánh xạ (Binding) - Tìm control theo ID đã đặt trong XML
        etUser = findViewById(R.id.etUsername)
        etPass = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        btnReg = findViewById(R.id.btnRegister)

        // Sự kiện Click nút Đăng Ký
        btnReg.setOnClickListener {
            val user = etUser.text.toString()
            val pass = etPass.text.toString()
            // Lấy view gốc để Snackbar bám vào
            val view = findViewById<android.view.View>(android.R.id.content)

            if (user.isEmpty() || pass.isEmpty()) {
                // --- Snackbar CẢNH BÁO: Thiếu thông tin ---
                val snackbarWarning = com.google.android.material.snackbar.Snackbar.make(view, "Vui lòng nhập đủ tên đăng nhập và mật khẩu!", com.google.android.material.snackbar.Snackbar.LENGTH_SHORT)
                snackbarWarning.setBackgroundTint(android.graphics.Color.parseColor("#FF9800")) // Màu cam cảnh báo
                snackbarWarning.setTextColor(android.graphics.Color.WHITE)
                snackbarWarning.show()

            } else {
                // Thử insert vào DB
                val isInserted = db.insertUser(user, pass)

                if (isInserted) {
                    // --- Snackbar THÀNH CÔNG: Đăng ký OK ---
                    val snackbarSuccess = com.google.android.material.snackbar.Snackbar.make(view, "Đăng ký tài khoản thành công!", com.google.android.material.snackbar.Snackbar.LENGTH_SHORT)
                    snackbarSuccess.setBackgroundTint(android.graphics.Color.parseColor("#4CAF50")) // Màu xanh lá
                    snackbarSuccess.show()

                    // Xóa trắng mật khẩu sau khi đăng ký thành công
                    etPass.text.clear()

                } else {
                    // --- Snackbar LỖI: Tài khoản đã tồn tại ---
                    val snackbarError = com.google.android.material.snackbar.Snackbar.make(view, "Tên đăng nhập này đã tồn tại!", com.google.android.material.snackbar.Snackbar.LENGTH_SHORT)
                    snackbarError.setBackgroundTint(android.graphics.Color.parseColor("#F44336")) // Màu đỏ
                    snackbarError.show()
                }
            }
        }

        // Sự kiện Click nút Login
        btnLogin.setOnClickListener {
            val user = etUser.text.toString()
            val pass = etPass.text.toString()
            val view = findViewById<android.view.View>(android.R.id.content)

            // Kiểm tra xem user có tồn tại không
            if (db.checkUsernameExists(user)) {
                // Nếu user tồn tại, kiểm tra tiếp mật khẩu
                if (db.checkUser(user, pass)) {
                    // MẬT KHẨU ĐÚNG -> ĐĂNG NHẬP THÀNH CÔNG
                    val snackbarSuccess = com.google.android.material.snackbar.Snackbar.make(view, "Đăng nhập thành công!", com.google.android.material.snackbar.Snackbar.LENGTH_LONG)
                    snackbarSuccess.setAction("OK") { /* Chuyển màn hình ở đây */ }
                    snackbarSuccess.setBackgroundTint(android.graphics.Color.parseColor("#4CAF50"))
                    snackbarSuccess.setActionTextColor(android.graphics.Color.WHITE)
                    snackbarSuccess.show()
                    etPass.text.clear()
                    // Tạo Intent để chuyển sang ProfileActivity
                    val intent = Intent(this, ProfileActivity::class.java)
                    // Gửi kèm tên người dùng sang màn hình mới
                    intent.putExtra("USERNAME_EXTRA", user)
                    startActivity(intent)
                } else {
                    // MẬT KHẨU SAI -> BÁO LỖI SAI MẬT KHẨU
                    val snackbarError = com.google.android.material.snackbar.Snackbar.make(view, "Sai mật khẩu!", com.google.android.material.snackbar.Snackbar.LENGTH_LONG)
                    snackbarError.setAction("Thử lại") {}
                    snackbarError.setBackgroundTint(android.graphics.Color.parseColor("#F44336"))
                    snackbarError.setTextColor(android.graphics.Color.WHITE)
                    snackbarError.setActionTextColor(android.graphics.Color.YELLOW)
                    snackbarError.show()
                    etPass.text.clear()
                }
            } else {
                // Nếu user KHÔNG tồn tại -> GỢI Ý ĐĂNG KÝ
                val snackbarRegister = com.google.android.material.snackbar.Snackbar.make(view, "Tài khoản không tồn tại!", com.google.android.material.snackbar.Snackbar.LENGTH_LONG)

                // Thêm hành động "Đăng ký"
                snackbarRegister.setAction("Đăng ký") {
                    // Tự động thực hiện hành động click nút đăng ký
                    btnReg.performClick()
                }

                snackbarRegister.setBackgroundTint(android.graphics.Color.parseColor("#2196F3")) // Màu xanh dương
                snackbarRegister.setActionTextColor(android.graphics.Color.YELLOW)
                snackbarRegister.show()
            }
        }
    }
}