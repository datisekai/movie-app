package com.example.movieapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.movieapp.data.model.HistoryDTO

class DBHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object{
        private const val DATABASE_NAME = "appMoviedatabase.db"
        private const val DATABASE_VERSION = 2

        private const val TABLE_NAME = "history"
        private const val COLUMN_ID = "id"
        private const val COLUMN_USERID = "userId"
        private const val COLUMN_ITEM_ID = "item_id"
        private const val COLUMN_TIME = "ep_saveTime"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_USERID INTEGER NOT NULL, $COLUMN_ITEM_ID INTEGER, $COLUMN_TIME LONG);"
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addUser(userID: Int): Long {
        val values = ContentValues()
        values.put(COLUMN_USERID, userID)

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
        val selection = "$COLUMN_USERID=? AND $COLUMN_ITEM_ID=?"
        val selectionArgs = arrayOf(userId.toString(), itemId.toString())
        val cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null)
        if (cursor != null && cursor.count > 0) {
            // Bản ghi đã tồn tại, không thêm vào cơ sở dữ liệu
            cursor.close()
            return -1
        }

        val values = ContentValues().apply {
            put(COLUMN_USERID, userId)
            put(COLUMN_ITEM_ID, itemId)
        }
        val insertedRowId = db.insert(TABLE_NAME, null, values)
        db.close()
        return insertedRowId
    }

    fun getListId(userID: Int): List<Int> {
        val listId = mutableListOf<Int>()

        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT $COLUMN_ITEM_ID FROM $TABLE_NAME WHERE $COLUMN_USERID=?", arrayOf(userID.toString()))

        if (cursor != null) {
            while (cursor.moveToNext()) {
                val itemID = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ITEM_ID))
                listId.add(itemID)
            }
            cursor.close()
        }
        db.close()
        return listId
    }

    fun getUserID(userId: Int): Long {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT $COLUMN_ID FROM $TABLE_NAME WHERE $COLUMN_USERID=?", arrayOf(userId.toString()))

        var userID: Long = -1
        if (cursor != null && cursor.moveToFirst()) {
            userID = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID))
            cursor.close()
        }
        db.close()
        return userID
    }

    fun deleteItemID(userID: Int, id: Int): Boolean {
        val db = this.writableDatabase

        val whereClause = "$COLUMN_USERID=? AND $COLUMN_ITEM_ID=?"
        val whereArgs = arrayOf(userID.toString(), id.toString())

        val deletedRows = db.delete(TABLE_NAME, whereClause, whereArgs)

        db.close()
        return deletedRows > 0
    }

    fun CheckIds(id: Int): Boolean {
        val db = this.readableDatabase
        val selection = "$COLUMN_ITEM_ID=?"
        val selectionArgs = arrayOf(id.toString())
        val cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null)

        val itemExists = cursor != null && cursor.count > 0
        cursor?.close()
        db.close()
        return itemExists
    }
    fun deleteAll(): Int {
        val db = this.writableDatabase
        val deletedRows = db.delete(TABLE_NAME, null, null)
        db.close()
        return deletedRows
    }

    fun addTime(userID: Int, itemId: Int, saveTime: Long): Long{
        val db = this.writableDatabase

        val values = ContentValues().apply {
            put(COLUMN_USERID,userID)
            put(COLUMN_ITEM_ID, itemId)
            put(COLUMN_TIME, saveTime)
        }
        val executeQuery = 0

        //Check has UserId and EpisodeId
        val selection = "$COLUMN_USERID=? AND $COLUMN_ITEM_ID=?"
        val selectionArgs = arrayOf(userID.toString(), itemId.toString())
        Log.e("Selection Array Time",selectionArgs.toString())
        val cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null,  null, null)
        if(cursor != null && cursor.count > 0){
            val executeQuery = db.update(TABLE_NAME,values,"$COLUMN_USERID=? AND $COLUMN_ITEM_ID=?", arrayOf(userID.toString(),itemId.toString()))
        }else{
            val executeQuery = db.insert(TABLE_NAME, null, values)
        }
        cursor.close()
        db.close()

        return executeQuery.toLong()
    }

    fun getHistoryByUserIdandItemId(userID: Int, itemId: Int): HistoryDTO{
        val result = HistoryDTO()
        val db = this.readableDatabase
        val cursor = db.query(TABLE_NAME, null, "$COLUMN_USERID=? AND $COLUMN_ITEM_ID=?", arrayOf(userID.toString(), itemId.toString()), null, null, null)
        if(cursor != null && cursor.moveToFirst()){
            result.Id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            result.userID = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USERID))
            result.itemId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ITEM_ID))
            result.time = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_TIME))
        }
        cursor.close()
        db.close()

        return result
    }

    fun getAll() : MutableList<HistoryDTO>{
        val db = this.readableDatabase

        val result = mutableListOf<HistoryDTO>()

        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)
        while(cursor.moveToNext()){
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val userId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USERID))
            val itemId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ITEM_ID))
            val time = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_TIME))
            val ep = HistoryDTO(id,userId,itemId,time)
            Log.e("Print History", ep.toString())
            result.add(ep)
        }
        cursor.close()
        db.close()

        return result
    }

}