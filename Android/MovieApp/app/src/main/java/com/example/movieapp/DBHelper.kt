package com.example.movieapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object{
        private const val DATABASE_NAME = "movieAppDatabase.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_NAME = "history"
        private const val COLUMN_ID = "id"
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_ITEM_ID = "item_id"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_USERNAME TEXT NOT NULL, $COLUMN_ITEM_ID INTEGER);"
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addUser(username: String): Long{
        val values= ContentValues()
        values.put(COLUMN_USERNAME, username)

        val db = this.writableDatabase
        val rowId = db.insert(TABLE_NAME, null, values)
        db.close()

        return rowId
    }

    fun getUser(username: String): Long{
        var userId: Long = -1
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT $COLUMN_ITEM_ID FROM $TABLE_NAME WHERE $COLUMN_USERNAME =?", arrayOf(username))
        if(cursor != null && cursor.moveToFirst()){
            userId = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID))
        }
        return userId
    }

    fun addItemId(userID: Long, id: Int): Int{
        val values = ContentValues()
        values.put(COLUMN_ITEM_ID, id)
        val db = this.writableDatabase
        val rowsAffected = db.update(TABLE_NAME, values, "$COLUMN_ID=?", arrayOf(userID.toString()))
        db.close()

        return rowsAffected
    }

    fun getItemId(userID: Long): List<Int>{
        val listId = mutableListOf<Int>()
        val db = this.writableDatabase
        val cursor = db.rawQuery("SELECT $COLUMN_ITEM_ID FROM $TABLE_NAME WHERE $COLUMN_ID=?", arrayOf(userID.toString()))
        if(cursor != null){
            while(cursor.moveToNext()){
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ITEM_ID))
                listId.add(id)
            }
            cursor.close()
        }
        return  listId
    }

    fun deleteId(userID: Long, id: Int): Boolean{
        val db = this.writableDatabase

        val whereClause = "$COLUMN_ID=? AND $COLUMN_ITEM_ID=?"
        val whereArgs = arrayOf(userID.toString(), id.toString())

        val deletedRows = db.delete(TABLE_NAME, whereClause, whereArgs)

        return deletedRows > 0
    }

}