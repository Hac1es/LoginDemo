package com.example.tunglete

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Ánh xạ View
        val tvWelcome = findViewById<TextView>(R.id.tvWelcome)
        val btnLogout = findViewById<Button>(R.id.btnLogout)

        // Lấy tên người dùng được gửi từ MainActivity
        val username = intent.getStringExtra("USERNAME_EXTRA")

        // Hiển thị lời chào
        tvWelcome.text = "Chào mừng, $username!"

        // Xử lý sự kiện nhấn nút Đăng xuất
        btnLogout.setOnClickListener {
            // Tạo Intent để quay về MainActivity
            val intent = Intent(this, MainActivity::class.java)
            // Xóa tất cả các Activity khác trong stack và tạo một Task mới
            // -> Đảm bảo người dùng không thể bấm "Back" để quay lại trang Profile
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}
