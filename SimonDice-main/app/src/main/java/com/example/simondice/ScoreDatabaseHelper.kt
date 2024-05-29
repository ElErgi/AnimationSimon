package com.example.simondice

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ScoreDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "simon_dice.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "high_scores"
        private const val COLUMN_ID = "id"
        private const val COLUMN_SCORE = "score"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = ("CREATE TABLE $TABLE_NAME ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$COLUMN_SCORE INTEGER)")
        db?.execSQL(createTable)
        db?.execSQL("INSERT INTO $TABLE_NAME ($COLUMN_SCORE) VALUES (0)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun getHighScore(): Int {
        val db = this.readableDatabase
        val cursor = db.query(TABLE_NAME, arrayOf(COLUMN_SCORE), null, null, null, null, null)
        if (cursor.moveToFirst()) {
            val highScore = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SCORE))
            cursor.close()
            return highScore
        }
        cursor.close()
        return 0
    }

    fun updateHighScore(newHighScore: Int) {
        val db = this.writableDatabase
        val updateQuery = "UPDATE $TABLE_NAME SET $COLUMN_SCORE = $newHighScore WHERE $COLUMN_ID = 1"
        db.execSQL(updateQuery)
    }
}
