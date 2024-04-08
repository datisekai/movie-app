package com.example.movieapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object{
        private const val DATABASE_NAME = "appMoviedatabase.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_NAME = "history"
        private const val COLUMN_ID = "id"
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_ITEM_ID = "item_id"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_USERNAME INTEGER NOT NULL, $COLUMN_ITEM_ID INTEGER);"
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addUser(username: Int): Long {
        val values = ContentValues()
        values.put(COLUMN_USERNAME, username)

        val db = this.writableDatabase
        val rowId = db.insert(TABLE_NAME, null, values)
        db.close()

        return rowId
    }

    fun addItemID(userID: Long, id: Int): Int {
        val values = ContentValues()
        values.put(COLUMN_ITEM_ID, id)

        val db = this.writableDatabase
        val rowsAffected = db.update(TABLE_NAME, values, "$COLUMN_ID=?", arrayOf(userID.toString()))
        db.close()

        return rowsAffected
    }
    //return -1 if fail
    fun insert(userId: Int, itemId: Int): Long {
        val db = this.writableDatabase

        //Check Duplicate
        val selection = "$COLUMN_USERNAME=? AND $COLUMN_ITEM_ID=?"
        val selectionArgs = arrayOf(userId.toString(), itemId.toString())
        val cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null)
        if (cursor != null && cursor.count > 0) {
            // Bản ghi đã tồn tại, không thêm vào cơ sở dữ liệu
            cursor.close()
            return -1
        }

        val values = ContentValues().apply {
            put(COLUMN_USERNAME, userId)
            put(COLUMN_ITEM_ID, itemId)
        }
        val insertedRowId = db.insert(TABLE_NAME, null, values)
        db.close()
        return insertedRowId
    }

    fun getListId(userID: Int): List<Int> {
        val listId = mutableListOf<Int>()

        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT $COLUMN_ITEM_ID FROM $TABLE_NAME WHERE $COLUMN_USERNAME=?", arrayOf(userID.toString()))

        if (cursor != null) {
            while (cursor.moveToNext()) {
                val itemID = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ITEM_ID))
                listId.add(itemID)
            }
            cursor.close()
        }

        return listId
    }

    fun getUserID(username: Int): Long {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT $COLUMN_ID FROM $TABLE_NAME WHERE $COLUMN_USERNAME=?", arrayOf(username.toString()))

        var userID: Long = -1
        if (cursor != null && cursor.moveToFirst()) {
            userID = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID))
            cursor.close()
        }

        return userID
    }

    fun deleteItemID(userID: Int, id: Int): Boolean {
        val db = this.writableDatabase

        val whereClause = "$COLUMN_USERNAME=? AND $COLUMN_ITEM_ID=?"
        val whereArgs = arrayOf(userID.toString(), id.toString())

        val deletedRows = db.delete(TABLE_NAME, whereClause, whereArgs)

        return deletedRows > 0
    }

    fun CheckIds(id: Int): Boolean {
        val db = this.readableDatabase
        val selection = "$COLUMN_ITEM_ID=?"
        val selectionArgs = arrayOf(id.toString())
        val cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null)

        val itemExists = cursor != null && cursor.count > 0
        cursor?.close()

        return itemExists
    }
    fun deleteAll(): Int {
        val db = this.writableDatabase
        val deletedRows = db.delete(TABLE_NAME, null, null)
        db.close()
        return deletedRows
    }

}