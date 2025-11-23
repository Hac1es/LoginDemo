package com.example.tunglete

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

// Kế thừa SQLiteOpenHelper để quản lý DB
class DBHelper(context: Context) : SQLiteOpenHelper(context, "SinhVienDB", null, 1) {

    // Giống lúc ông chạy script tạo bảng trong SQL Server
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE users(username TEXT PRIMARY KEY, password TEXT)")
    }

    // Cái này để update version DB, tạm thời kệ mẹ nó
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS users")
        onCreate(db)
    }

    // Hàm Insert (Đăng ký)
    fun insertUser(user: String, pass: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("username", user)
        values.put("password", pass)

        // Trả về -1 là lỗi, khác -1 là ngon
        val result = db.insert("users", null, values)
        return result != -1L
    }

    // Hàm Check Login (Select count)
    fun checkUser(user: String, pass: String): Boolean {
        val db = this.readableDatabase
        // rawQuery giống hệt viết câu lệnh SQL thường
        val cursor = db.rawQuery("SELECT * FROM users WHERE username = ? AND password = ?", arrayOf(user, pass))
        val result = cursor.count > 0
        cursor.close() // Nhớ đóng kết nối
        return result
    }

    // Hàm này chỉ kiểm tra xem username đã tồn tại trong DB chưa
    fun checkUsernameExists(username: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM users WHERE username = ?", arrayOf(username))
        val userExists = cursor.count > 0
        cursor.close()
        return userExists
    }

}