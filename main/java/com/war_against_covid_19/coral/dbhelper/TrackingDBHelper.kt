package com.war_against_covid_19.coral.dbhelper




import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.util.*

class TrackingDBHelper(context: Context) : SQLiteOpenHelper(context, "tracking.db", null, 4) {//marker
    val TAG = "DbHelper"
    val TABLE = "track"
 val THRESHOLD=100
    companion object {
        val ID: String = "_id"
        val TIMESTAMP: String = "TIMESTAMP"
        val TEXT: String = "TEXT"
        val LONGITUDE: String= "LONGITUDE"
        val LATITUDE: String= "LATITUDE"
    }

    val DATABASE_CREATE =
            "CREATE TABLE if not exists " + TABLE + " (" +
                    "${ID} integer PRIMARY KEY autoincrement," +
                    "${TIMESTAMP} integer," +
                    "${TEXT} text,"+
                    "${LONGITUDE} float,"+
                    "${LATITUDE} float"+
                    ")"
    fun record(text: String,longitude:Float,latitude:Float) {
        val values = ContentValues()
        values.put(TEXT, text)
        values.put(TIMESTAMP, Date().time)
        values.put(LONGITUDE,longitude)
        values.put(LATITUDE,latitude)
        getWritableDatabase().insert(TABLE, null, values);
    }
    fun record(text: String) {
        val values = ContentValues()
        values.put(TEXT, text)
        values.put(TIMESTAMP, Date().time)
        getWritableDatabase().insert(TABLE, null, values);//add if <threshold then not add
    }
    //longitude, latitude threshold(100 meter)
    fun getLogs() : Cursor {
        return getReadableDatabase()
                .query(TABLE, arrayOf(ID, TIMESTAMP, TEXT), null, null, null, null, TIMESTAMP + " DESC");
    }
    fun getRecords() : Cursor {
        return getReadableDatabase()
            .query(TABLE, arrayOf( TIMESTAMP, LONGITUDE, LATITUDE), null, null, null, null, TIMESTAMP + " DESC");
    }
    fun getRecords(begin:Long,end:Long) : Cursor {
        return getReadableDatabase()
            .query(TABLE, arrayOf( TIMESTAMP, LONGITUDE, LATITUDE), null, null, null, null, TIMESTAMP + " DESC");
    }
    override fun onCreate(db: SQLiteDatabase) {
        Log.d(TAG, "Creating: " + DATABASE_CREATE);
        db.execSQL(DATABASE_CREATE)
    }

    override fun onUpgrade(p0: SQLiteDatabase, p1: Int, p2: Int) {
    }

}
