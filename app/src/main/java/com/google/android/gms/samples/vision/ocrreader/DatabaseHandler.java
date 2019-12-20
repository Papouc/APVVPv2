package com.google.android.gms.samples.vision.ocrreader;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseHandler extends SQLiteOpenHelper {


    private static final String TABLE_NAME = "HistoryRecords";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Historie.db";


    //polozky
    private static final String COLUMN_POLOZKA = "Uloha";
    private static final String COLUMN_CAS = "CasZadani";
    private static final String COLUMN_ID = "id";





    public DatabaseHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL("CREATE TABLE "+TABLE_NAME+" ( "+COLUMN_ID+" TEXT, "+COLUMN_CAS+" TEXT)");
        //db.execSQL("CREATE TABLE "+TABLE_NAME+" ("+COLUMN_POLOZKA+" TEXT, "+COLUMN_CAS+" TEXT)" );


         db.execSQL("CREATE TABLE "+TABLE_NAME+" (" +COLUMN_ID+ " INTEGER PRIMARY KEY, " +COLUMN_POLOZKA+" TEXT, "+COLUMN_CAS+" TEXT)");
        //db.execSQL("create table " + TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,Uloha TEXT,CasZadani TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }


    public boolean insertData(String uloha, String cas) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_POLOZKA, uloha);
        contentValues.put(COLUMN_CAS, cas);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }


    public Cursor test() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from HistoryRecords", null);
        return cursor;
    }


}
