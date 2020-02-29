package com.google.android.gms.samples.vision.ocrreader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

public class Nastav extends AppCompatActivity {

    public Switch RotaceSwitch;
    public Switch HistorieSwitch;
    public static SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nastav);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        RotaceSwitch = (Switch)findViewById(R.id.rotSwitch);
        HistorieSwitch = (Switch)findViewById(R.id.hisSwitch);
        pref = getApplicationContext().getSharedPreferences("Nastavko", 0);
        editor = pref.edit();

        Boolean Otacko = pref.getBoolean("MamToOtacet", true);
        Boolean Historko = pref.getBoolean("MamZapisovatDoHistorie", true);
        Log.d("TestNastaveni", String.valueOf(Otacko));
        Log.d("TestNastaveni", String.valueOf(Historko));

        RotaceSwitch.setChecked(Otacko);
        HistorieSwitch.setChecked(Historko);

        RotaceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("MamToOtacet", isChecked);
                OcrCaptureActivity.ZmenaNastavZaBehuOtocka = isChecked;
                editor.commit();

            }
        });

        HistorieSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("MamZapisovatDoHistorie", isChecked);
                UpravaZadaniActivity.HistoriePodleNastaveni = isChecked;
                editor.commit();
            }
        });







    }






}
