package com.google.android.gms.samples.vision.ocrreader;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


public class HistorieActivity extends AppCompatActivity {


   public static ListView HistoryList;
   public static ArrayList<String> arrayList;
   public static ArrayAdapter arrayAdapter;
   public DatabaseHandler db;
   public  String[] ulohy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historie);


            db = new DatabaseHandler(this);

            HistoryList = (ListView) findViewById(R.id.HistoryList);

            showHistory();


    }

    public void showHistory() {
        Cursor cursor = db.readDat();

        if (cursor.getCount() != 0) { // když v databázi něco je
            if (cursor.moveToFirst()) {
                ulohy = new String[cursor.getCount()];
                int i = 0;
                do {
                    ulohy[i] = cursor.getString(1);
                    i++;
                } while (cursor.moveToNext());
            }
            arrayList = new ArrayList<String>(Arrays.asList(ulohy));
            Collections.reverse(arrayList);
            arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
            HistoryList.setAdapter(arrayAdapter);

        }

    }




}
