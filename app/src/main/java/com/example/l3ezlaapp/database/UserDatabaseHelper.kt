package com.example.l3ezlaapp.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.security.MessageDigest

class UserDatabaseHelper(context: Context?) : SQLiteOpenHelper(context, "SignLog.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("create Table users(email TEXT primary key, username TEXT, password TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("drop Table if exists users")
    }

    // Hashes a password using the SHA-256 algorithm
    private fun hashPassword(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashedBytes = digest.digest(password.toByteArray())
        return hashedBytes.joinToString("") { "%02x".format(it) }
    }

    fun insertData(email: String, username: String, password: String): Boolean {
        val MyDatabase = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("email", email)
        contentValues.put("username", username)
        contentValues.put("password", hashPassword(password)) // Hash the password before storing
        val result = MyDatabase.insert("users", null, contentValues)
        return result != -1L
    }

    fun checkEmail(email: String): Boolean {
        val MyDatabase = this.writableDatabase
        val cursor = MyDatabase.rawQuery("Select * from users where email = ?", arrayOf(email))
        return cursor.count > 0
    }

    fun checkEmailPassword(email: String, password: String): Boolean {
        val MyDatabase = this.writableDatabase
        val hashedPassword = hashPassword(password) // Hash the password to match with the stored hashed password
        val cursor = MyDatabase.rawQuery("Select * from users where email = ? and password = ?", arrayOf(email, hashedPassword))
        return cursor.count > 0
    }

    fun checkUsername(username: String): Boolean {
        val MyDatabase = this.writableDatabase
        val cursor = MyDatabase.rawQuery("Select * from users where username = ?", arrayOf(username))
        return cursor.count > 0
    }

    fun checkCredentials(userInput: String, password: String): Boolean {
        val MyDatabase = this.writableDatabase
        val hashedPassword = hashPassword(password) // Hash the password to match with the stored hashed password
        val cursor = MyDatabase.rawQuery("Select * from users where (email = ? or username = ?) and password = ?", arrayOf(userInput, userInput, hashedPassword))
        return cursor.count > 0
    }
}
