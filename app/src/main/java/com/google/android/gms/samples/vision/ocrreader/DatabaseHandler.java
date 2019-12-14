package com.google.android.gms.samples.vision.ocrreader;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DatabaseHandler extends SQLiteOpenHelper {


    private static final String TABLE_NAME = "HistoryRecords";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Historie.db";


    //polozky
    private static final String COLUMN_POLOZKA = "Uloha";
    private static final String COLUMN_CAS = "CasZadani";
    private static final String COLUMN_ID = "id";


    SQLiteDatabase database;


    public DatabaseHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        database = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL("CREATE TABLE "+TABLE_NAME+" ( "+COLUMN_ID+" TEXT, "+COLUMN_COUNTRY+" TEXT)");

       // db.execSQL("CREATE TABLE "+TABLE_NAME+" ("+COLUMN_POLOZKA+" TEXT, "+COLUMN_CAS+" TEXT)" );

        db.execSQL("CREATE TABLE "+TABLE_NAME+" (" +COLUMN_ID+ " INTEGER PRIMARY KEY, " +COLUMN_POLOZKA+" TEXT, "+COLUMN_CAS+" TEXT)" );

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_POLOZKA, "fffffffffffffffffffffffff");

        db.insert(TABLE_NAME, null, contentValues);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }
}
