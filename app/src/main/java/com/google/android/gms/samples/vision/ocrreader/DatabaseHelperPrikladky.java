package com.google.android.gms.samples.vision.ocrreader;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;





public class DatabaseHelperPrikladky extends SQLiteOpenHelper {


    private static final String COLUMN_POLOZKA = "Uloha";
    private static final String COLUMN_CAS = "CasZadani";
    private static final String TABLE_NAME = "Ulohy";
    private static final String COLUMN_ID = "id";

    public DatabaseHelperPrikladky(@Nullable Context context) {
        super(context, "PrikladkyDatabaze", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLE_NAME+" (" +COLUMN_ID+ " INTEGER PRIMARY KEY, " +COLUMN_POLOZKA+" TEXT, "+COLUMN_CAS+" TEXT)");
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

    public Cursor readDat() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME, null);
        return cursor;
    }
}