package com.labs.poziom.whereabouts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.os.Build.ID;
import static com.labs.poziom.whereabouts.DBStorage.WIFI_ALIAS;
import static com.labs.poziom.whereabouts.DBStorage.WIFI_COLUMN_ID;
import static com.labs.poziom.whereabouts.DBStorage.WIFI_SSID;
import static com.labs.poziom.whereabouts.R.id.ssid;

/**
 * Created by Sandy on 10-01-2017.
 */

public class DbContacts extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "ContactsInfo";
    private static final int DATABASE_VERSION = 2;
    public static final String TABLE_NAME = "FrequentContacts";
    public static final String CONTACT_NAME= "name";
    public static final String CONTACT_ID = "id";
    public static final String CONTACT_NUMBER = "phone";

    public DbContacts(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" +
                CONTACT_ID + " TEXT, " + CONTACT_NUMBER + " TEXT, " +
                CONTACT_NAME + " TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertContact(String id, String name, String phone) {
        SQLiteDatabase db = getWritableDatabase();


        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTACT_ID, id);
        contentValues.put(CONTACT_NAME, name);
        contentValues.put(CONTACT_NUMBER, phone);
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




    public Cursor getAllContacts() {
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

}
