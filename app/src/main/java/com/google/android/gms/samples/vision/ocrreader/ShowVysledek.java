package com.google.android.gms.samples.vision.ocrreader;

import android.content.Intent;
import android.os.Bundle;


import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
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
    //private AdView mAdView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_vysledek);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /*Intent intent4 = getIntent();
        TohleUzJeFaktVysledek = intent4.getStringExtra("Vysledecek");*/

        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");

        /*mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);*/








        TextView PolickoNaVysledecek = findViewById(R.id.SemSVysledkem);

        TohleUzJeFaktVysledek = OcrCaptureActivity.TenhleVysledekFaktPlati;

        PolickoNaVysledecek.setText("Výsledek vaší úlohy je :       " + TohleUzJeFaktVysledek);

    }

}
