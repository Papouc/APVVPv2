package com.google.android.gms.samples.vision.ocrreader;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;

public class Nastav extends AppCompatActivity {

    DatabaseNastavHandler databaseNastavHandler;
    public String NastaveniStrRot;
    public String NastaveniStrHis;
    public Switch RotaceSwitch;
    public Switch HistorieSwitch;
    public static  boolean OtaceniBool = true;
    public static  boolean HistorieBool = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nastav);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        RotaceSwitch = (Switch)findViewById(R.id.rotSwitch);
        HistorieSwitch = (Switch)findViewById(R.id.hisSwitch);
        showNastav();

        RotaceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if (isChecked == true) {
                   ZapisDoNastaveni("true", String.valueOf(HistorieSwitch.isChecked()));
               } else {
                   ZapisDoNastaveni("false", String.valueOf(HistorieSwitch.isChecked()));
               }
            }
        });

        HistorieSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
    }

    public void ZapisDoNastaveni(String rotace, String historie) {

        boolean isIserted = OcrCaptureActivity.databaseNastavHandler.insertData(rotace, historie);
        if (isIserted == true)
            //Toast.makeText(UpravaZadaniActivity.this,"Data Inserted", Toast.LENGTH_LONG).show();
            Log.d("Data", "Data inserted");
        else
            //Toast.makeText(UpravaZadaniActivity.this,"Data not Inserted", Toast.LENGTH_LONG).show();
            Log.d("Data", "Data not inserted");


    }

    public void showNastav() {
        Cursor cursor = OcrCaptureActivity.databaseNastavHandler.readDat();
        if (cursor.getCount() == 0) {
            Log.d("prazdno", "nic tam neni");
            ZapisDoNastaveni("true", "true");
            HistorieBool = true;
            OtaceniBool = true;
        } else {
            while (cursor.moveToNext()) {


                NastaveniStrRot = cursor.getString(0);
                NastaveniStrHis = cursor.getString(1);

                Log.d("cotamjeRot", NastaveniStrRot);
                Log.d("cotamjeHis", NastaveniStrHis);



            }

        }




    }


}
