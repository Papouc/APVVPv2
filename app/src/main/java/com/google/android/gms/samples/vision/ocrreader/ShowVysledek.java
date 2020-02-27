package com.google.android.gms.samples.vision.ocrreader;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.TextView;


public class ShowVysledek extends AppCompatActivity {

    public String TohleUzJeFaktVysledek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_vysledek);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*Intent intent4 = getIntent();
        TohleUzJeFaktVysledek = intent4.getStringExtra("Vysledecek");*/

        TextView PolickoNaVysledecek = findViewById(R.id.SemSVysledkem);

        TohleUzJeFaktVysledek = OcrCaptureActivity.TenhleVysledekFaktPlati;

        PolickoNaVysledecek.setText("Výsledek vaší úlohy je :       " + TohleUzJeFaktVysledek);

    }

}
