package com.google.android.gms.samples.vision.ocrreader;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Calendar;

public class UpravaZadaniActivity extends AppCompatActivity {


    public  static EditText PoleProUlohu;
    public  static Button OdstranitTextBut;
    public String NaskanovanaUloha;
    final Handler handler = new Handler();
    public static ArrayList<String> ProPridaniDoHistorie = new ArrayList<>();
    public static ContentValues Dodatabaze = new ContentValues();
    public static Button VyresBut;
    public int random;
    DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uprava_zadani);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        VyresBut = (Button) findViewById(R.id.ReseniBut);
        databaseHandler = new DatabaseHandler(this);

        PoleProUlohu = (EditText) findViewById(R.id.PoleProUpravu);
        Button ButtonPridat = findViewById(R.id.ReseniBut);
        Intent intent3 = getIntent();
        NaskanovanaUloha = intent3.getStringExtra(OcrCaptureActivity.EXTRA_TEXT);
        UpravTextHackyCarky();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                PoleProUlohu.setText(NaskanovanaUloha);
                OdstranitTextBut = (Button) findViewById(R.id.DeleteBut);
                OdstranitTextBut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        VymazatZadani();
                    }
                });
            }
        }, 300);




    }

    public void ZapisDoGarbage(String TaUloha) {

        Date currentTime = Calendar.getInstance().getTime();
        boolean isIserted = databaseHandler.insertData(TaUloha,String.valueOf(currentTime));
        if (isIserted == true)
            Toast.makeText(UpravaZadaniActivity.this,"Data Inserted", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(UpravaZadaniActivity.this,"Data not Inserted", Toast.LENGTH_LONG).show();

        OcrCaptureActivity.garbageDatabase = FirebaseDatabase.getInstance("https://apvvp-garbage.firebaseio.com/").getReference(String.valueOf(random));
        OcrCaptureActivity.garbageDatabase.setValue(TaUloha);

    }


    public  void VymazatZadani() {
        if (NaskanovanaUloha != null) {
            OcrDetectorProcessor.GlobalUlohaText = null;
            OcrCaptureActivity.TextProUpravu = null;
            NaskanovanaUloha = null;
            PoleProUlohu.setText(null);
        }

    }




    public  void UpravTextHackyCarky() {

        Random rand = new Random();
        random = rand.nextInt(32000);

        if (NaskanovanaUloha != null) {

            // Ucka
            NaskanovanaUloha = NaskanovanaUloha.replace("Ù", "U");
            NaskanovanaUloha = NaskanovanaUloha.replace("Ú", "U");
            NaskanovanaUloha = NaskanovanaUloha.replace("Û", "U");
            NaskanovanaUloha = NaskanovanaUloha.replace("Ü", "U");
            NaskanovanaUloha = NaskanovanaUloha.replace("Ư", "U");
            NaskanovanaUloha = NaskanovanaUloha.replace("Ů", "U");
            NaskanovanaUloha = NaskanovanaUloha.replace("Ū", "U");
            NaskanovanaUloha = NaskanovanaUloha.replace("Ů", "U");
            NaskanovanaUloha = NaskanovanaUloha.replace("Ų", "U");
            NaskanovanaUloha = NaskanovanaUloha.replace("Ű", "U");
            NaskanovanaUloha = NaskanovanaUloha.replace("ů", "u");
            NaskanovanaUloha = NaskanovanaUloha.replace("ú", "u");
            NaskanovanaUloha = NaskanovanaUloha.replace("ü", "u");
            NaskanovanaUloha = NaskanovanaUloha.replace("ū", "u");
            NaskanovanaUloha = NaskanovanaUloha.replace("ų", "u");
            NaskanovanaUloha = NaskanovanaUloha.replace("ű", "u");

            // Acka
            NaskanovanaUloha = NaskanovanaUloha.replace("À", "A");
            NaskanovanaUloha = NaskanovanaUloha.replace("Á", "A");
            NaskanovanaUloha = NaskanovanaUloha.replace("Â", "A");
            NaskanovanaUloha = NaskanovanaUloha.replace("Ã", "A");
            NaskanovanaUloha = NaskanovanaUloha.replace("Ä", "A");
            NaskanovanaUloha = NaskanovanaUloha.replace("Å", "A");
            NaskanovanaUloha = NaskanovanaUloha.replace("à", "a");
            NaskanovanaUloha = NaskanovanaUloha.replace("á", "a");
            NaskanovanaUloha = NaskanovanaUloha.replace("â", "a");
            NaskanovanaUloha = NaskanovanaUloha.replace("ã", "a");
            NaskanovanaUloha = NaskanovanaUloha.replace("ä", "a");
            NaskanovanaUloha = NaskanovanaUloha.replace("å", "a");

            // Icka
            NaskanovanaUloha = NaskanovanaUloha.replace("Ī", "I");
            NaskanovanaUloha = NaskanovanaUloha.replace("Į", "I");
            NaskanovanaUloha = NaskanovanaUloha.replace("İ", "I");
            NaskanovanaUloha = NaskanovanaUloha.replace("Í", "I");
            NaskanovanaUloha = NaskanovanaUloha.replace("ī", "i");
            NaskanovanaUloha = NaskanovanaUloha.replace("į", "i");
            NaskanovanaUloha = NaskanovanaUloha.replace("ı", "i");
            NaskanovanaUloha = NaskanovanaUloha.replace("ί", "i");
            NaskanovanaUloha = NaskanovanaUloha.replace("í", "i");

            // Ecka
            NaskanovanaUloha = NaskanovanaUloha.replace("Ē", "E");
            NaskanovanaUloha = NaskanovanaUloha.replace("ē", "e");
            NaskanovanaUloha = NaskanovanaUloha.replace("Ė", "E");
            NaskanovanaUloha = NaskanovanaUloha.replace("ė", "e");
            NaskanovanaUloha = NaskanovanaUloha.replace("Ę", "E");
            NaskanovanaUloha = NaskanovanaUloha.replace("ę", "e");
            NaskanovanaUloha = NaskanovanaUloha.replace("Ě", "E");
            NaskanovanaUloha = NaskanovanaUloha.replace("ě", "e");
            NaskanovanaUloha = NaskanovanaUloha.replace("É", "E");
            NaskanovanaUloha = NaskanovanaUloha.replace("é", "e");

            // Ocka
            NaskanovanaUloha = NaskanovanaUloha.replace("Ō", "O");
            NaskanovanaUloha = NaskanovanaUloha.replace("ō", "o");
            NaskanovanaUloha = NaskanovanaUloha.replace("Ő", "O");
            NaskanovanaUloha = NaskanovanaUloha.replace("ő", "o");
            NaskanovanaUloha = NaskanovanaUloha.replace("Ó", "O");
            NaskanovanaUloha = NaskanovanaUloha.replace("ó", "o");
            NaskanovanaUloha = NaskanovanaUloha.replace("ό", "o");
            NaskanovanaUloha = NaskanovanaUloha.replace("Θ", "O");
            NaskanovanaUloha = NaskanovanaUloha.replace("ò", "o");
            NaskanovanaUloha = NaskanovanaUloha.replace("ó", "o");
            NaskanovanaUloha = NaskanovanaUloha.replace("ô", "o");
            NaskanovanaUloha = NaskanovanaUloha.replace("õ", "o");
            NaskanovanaUloha = NaskanovanaUloha.replace("ö", "o");
            NaskanovanaUloha = NaskanovanaUloha.replace("Ò", "O");
            NaskanovanaUloha = NaskanovanaUloha.replace("Ó", "O");
            NaskanovanaUloha = NaskanovanaUloha.replace("Ô", "O");
            NaskanovanaUloha = NaskanovanaUloha.replace("Õ", "O");
            NaskanovanaUloha = NaskanovanaUloha.replace("Ö", "O");

            // to co nejsou souhlasky
            NaskanovanaUloha = NaskanovanaUloha.replace("Ž", "Z");
            NaskanovanaUloha = NaskanovanaUloha.replace("ž", "z");
            NaskanovanaUloha = NaskanovanaUloha.replace("Ť", "T");
            NaskanovanaUloha = NaskanovanaUloha.replace("ť", "t");
            NaskanovanaUloha = NaskanovanaUloha.replace("Š", "S");
            NaskanovanaUloha = NaskanovanaUloha.replace("š", "s");
            NaskanovanaUloha = NaskanovanaUloha.replace("Ř", "R");
            NaskanovanaUloha = NaskanovanaUloha.replace("ř", "r");
            NaskanovanaUloha = NaskanovanaUloha.replace("Ň", "N");
            NaskanovanaUloha = NaskanovanaUloha.replace("ň", "n");
            NaskanovanaUloha = NaskanovanaUloha.replace("Ď", "D");
            NaskanovanaUloha = NaskanovanaUloha.replace("ď", "d");
            NaskanovanaUloha = NaskanovanaUloha.replace("Č", "C");
            NaskanovanaUloha = NaskanovanaUloha.replace("č", "c");
            NaskanovanaUloha = NaskanovanaUloha.replace("ý", "y");
            NaskanovanaUloha = NaskanovanaUloha.replace("Ý", "Y");
            //NaskanovanaUloha = NaskanovanaUloha.toLowerCase();





            if (NaskanovanaUloha.charAt(0) == 'n' && NaskanovanaUloha.charAt(1) == 'u' && NaskanovanaUloha.charAt(2) == 'l' && NaskanovanaUloha.charAt(3) == 'l') {
               // Log.d("testicek",String.valueOf(NaskanovanaUloha.charAt(0)) + String.valueOf(NaskanovanaUloha.charAt(1)) + String.valueOf(NaskanovanaUloha.charAt(2)) + String.valueOf(NaskanovanaUloha.charAt(3)) );
               // Log.d("testicek",NaskanovanaUloha.substring(0,4));
                 NaskanovanaUloha = NaskanovanaUloha.replace(NaskanovanaUloha.substring(0,4),"");

            }

            VyresBut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ZapisDoGarbage(NaskanovanaUloha);
                }
            });







        }
        }

    }





