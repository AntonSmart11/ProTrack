package com.antonsmart.protrack.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.antonsmart.protrack.objects.User
import kotlin.Exception
import kotlin.collections.ArrayList

class SQLiteHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {

        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "protrack.db"

        //Table of users
        private const val TABLE_USERS = "users"
        private const val ID_USER = "id_users"
        private const val NAME_USER = "name"
        private const val LAST_USER = "last"
        private const val USERNAME_USER = "username"
        private const val PASSWORD_USER = "password"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        val TABLE_USERS = ("CREATE TABLE " + TABLE_USERS + "(" +
                ID_USER + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                NAME_USER + " TEXT," +
                LAST_USER + " TEXT," +
                USERNAME_USER + " TEXT," +
                PASSWORD_USER + " TEXT"
                +")")

        db?.execSQL(TABLE_USERS)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    fun InsertUser(user: User): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(NAME_USER, user.name)
        contentValues.put(LAST_USER, user.last)
        contentValues.put(USERNAME_USER, user.username)
        contentValues.put(PASSWORD_USER, user.password)

        val success = db.insert(TABLE_USERS, null, contentValues)
        db.close()
        return success
    }

    @SuppressLint("Range")
    fun GetAllUsers(): ArrayList<User> {
        val userList: ArrayList<User> = ArrayList()
        val selectQuery = "SELECT * FROM $TABLE_USERS"
        val db = this.readableDatabase

        val cursor: Cursor?

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: Exception) {
            e.printStackTrace()
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id_user: Int
        var name: String
        var last: String
        var username: String
        var password: String

        if(cursor.moveToFirst()) {
            do {
                id_user = cursor.getInt(cursor.getColumnIndex(ID_USER))
                name = cursor.getString(cursor.getColumnIndex(NAME_USER))
                last = cursor.getString(cursor.getColumnIndex(LAST_USER))
                username = cursor.getString(cursor.getColumnIndex(USERNAME_USER))
                password = cursor.getString(cursor.getColumnIndex(PASSWORD_USER))

                val user = User(id_user, name, last, username, password)
                userList.add(user)
            } while (cursor.moveToNext())
        }

        return userList
    }

    fun UpdateUser(user: User): Int {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(ID_USER, user.id)
        contentValues.put(NAME_USER, user.name)
        contentValues.put(LAST_USER, user.last)
        contentValues.put(USERNAME_USER, user.username)
        contentValues.put(PASSWORD_USER, user.password)

        val success = db.update(TABLE_USERS, contentValues, ID_USER + "=" + user.id, null)
        db.close()

        return success
    }

    fun DeleteUser(id: Int): Int {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(ID_USER, id)

        val success = db.delete(TABLE_USERS, ID_USER + "=" + id, null)
        db.close()

        return success
    }

}