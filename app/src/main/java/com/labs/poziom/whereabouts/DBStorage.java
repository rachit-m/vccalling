package com.labs.poziom.whereabouts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.R.attr.name;

/**
 * Created by Sandy on 10-01-2017.
 */

public class DBStorage extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "WifiDB";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "wifiAlias";
    public static final String WIFI_COLUMN_ID = "_id";
    public static final String WIFI_SSID = "ssid";
    public static final String WIFI_ALIAS = "alias";

    public DBStorage(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" +
                WIFI_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                WIFI_SSID + " TEXT, " +
                WIFI_ALIAS + " TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertWifi(String ssid, String name) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(WIFI_SSID, ssid);
        contentValues.put(WIFI_ALIAS, name);
        db.beginTransaction();
        try {
            // insert data here
            db.insert(TABLE_NAME, null, contentValues);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return true;
    }


    public boolean updateWifi(Integer id, String ssid, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(WIFI_SSID, ssid);
        contentValues.put(WIFI_ALIAS, name);
        db.update(TABLE_NAME, contentValues, WIFI_COLUMN_ID + " = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Cursor getProfile(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( "SELECT * FROM " + TABLE_NAME + " WHERE " +
                WIFI_COLUMN_ID + "=?", new String[] { Integer.toString(id) } );
        return res;
    }
    public Cursor getAllProfiles() {
        Cursor res=null;
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        try {
            res = db.rawQuery( "SELECT * FROM " + TABLE_NAME, null );
            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
        }

        return res;
    }
    public Integer deleteProfile(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,
                WIFI_COLUMN_ID + " = ? ",
                new String[] { Integer.toString(id) });
    }
}
