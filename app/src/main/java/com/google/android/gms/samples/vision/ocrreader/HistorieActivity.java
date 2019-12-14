package com.google.android.gms.samples.vision.ocrreader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class HistorieActivity extends AppCompatActivity {


   public static ListView HistoryList;
   public static ArrayList<String> arrayList;
   public static ArrayAdapter arrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historie);

        HistoryList = (ListView) findViewById(R.id.HistoryList);
        if (UpravaZadaniActivity.ProPridaniDoHistorie != null) {
            arrayList = UpravaZadaniActivity.ProPridaniDoHistorie;



        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);
        HistoryList.setAdapter(arrayAdapter);
        }


    }




}
